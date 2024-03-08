package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.bill.BillUserRequestDTO;
import com.clothes.websitequanao.dto.request.cart.CartRequestDTO;
import com.clothes.websitequanao.dto.request.cart.CartRequestNoLoginDTO;
import com.clothes.websitequanao.dto.response.bill.BillAdminResponseDto;
import com.clothes.websitequanao.dto.response.bill.BillDetailDto;
import com.clothes.websitequanao.dto.response.bill.BillResponseDto;
import com.clothes.websitequanao.dto.response.cart.CartResponseDto;
import com.clothes.websitequanao.email.SendEmailService;
import com.clothes.websitequanao.entity.*;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.*;
import com.clothes.websitequanao.service.BillService;
import com.clothes.websitequanao.utils.CodeUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.BillType.*;
import static com.clothes.websitequanao.common.Consts.FunctionStatus.ON;
import static com.clothes.websitequanao.common.Consts.ReceiptType.INCOMPLETE;
import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;
import static com.clothes.websitequanao.common.Consts.UserType.USER_INACTIVE;

@Service
@RequiredArgsConstructor
@Slf4j
public class BillServiceImpl implements BillService {

    private final UserRepo userRepo;

    private final UserRoleRepo userRoleRepo;

    private final ProductRepo productRepo;

    private final BillRepo billRepo;

    private final ProductBillRepo productBillRepo;

    private final SpecialityRepo specialityRepo;

    private final ProductSpecialityRepo productSpecialityRepo;

    private final SendEmailService sendEmailService;

    @Override
    public ServiceResponse getAll() {
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            // tim cart
            BillEntity cart = billRepo.findByUserIdAndStatus(userId, BILL_CART).orElse(null);
            if (cart == null) return ServiceResponse.RESPONSE_SUCCESS("Chưa có sản phẩm trong giỏ hàng");

            List<ProductBillEntity> billEntityList = productBillRepo.findByBillId(cart.getId());
            if (billEntityList.size() <= 0) return ServiceResponse.RESPONSE_SUCCESS("Chưa có sản phẩm trong giỏ hàng");

            List<CartResponseDto> result = new ArrayList<>();
            for (ProductBillEntity pbe : billEntityList) {
                ProductEntity product = productRepo.findById(pbe.getProductId()).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");

                if (product.getActive() == USER_INACTIVE) {
                    productBillRepo.delete(pbe);
                } else {
                    List<Long> speId = productSpecialityRepo.getByProductId(product.getId());

                    List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

                    Collections.sort(speciality);

                    CartResponseDto dto = CartResponseDto.builder()
                            .id(product.getId())
                            .parentId(product.getParentId())
                            .billId(cart.getId())
                            .productName(product.getProductName())
                            .img(product.getImg())
                            .quantity(pbe.getQuantity())
                            .priceSell(product.getPriceSell())
                            .totalQuantity(product.getQuantity())
                            .totalPrice(pbe.getTotalPrice())
                            .speciality(speciality)
                            .build();
                    if (product.getParentId() != null) {
                        ProductEntity parentProduct = productRepo.findById(product.getParentId()).orElse(null);
                        if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm cha không tồn tại");
                        dto.setParentName(parentProduct.getProductName());
                    }
                    result.add(dto);
                }
            }

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra sản phẩm trong giỏ hàng");
        }
    }

    @Override
    public ServiceResponse getAllNoLogin(CartRequestNoLoginDTO dto) {
        try {
            if (dto.getListProduct() == null) return ServiceResponse.RESPONSE_ERROR("Chưa chọn sản phẩm nào");
            List<CartResponseDto> result = new ArrayList<>();
            for (CartRequestDTO e : dto.getListProduct()) {
                ProductEntity product = productRepo.findByIdAndActive(e.getProductId(), ON).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("sản phẩm không tồn tại");
                List<Long> speId = productSpecialityRepo.getByProductId(product.getId());

                List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

                Collections.sort(speciality);
                CartResponseDto dto1 = CartResponseDto.builder()
                        .id(product.getId())
                        .parentId(product.getParentId())
                        .productName(product.getProductName())
                        .img(product.getImg())
                        .quantity(e.getQuantity())
                        .priceSell(product.getPriceSell())
                        .totalQuantity(product.getQuantity())
                        .totalPrice(product.getPriceSell().multiply(BigDecimal.valueOf(e.getQuantity())))
                        .speciality(speciality)
                        .build();
                if (product.getParentId() != null) {
                    ProductEntity parentProduct = productRepo.findById(product.getParentId()).orElse(null);
                    if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm cha không tồn tại");
                    dto1.setParentName(parentProduct.getProductName());
                }
                result.add(dto1);
            }


            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra sản phẩm trong giỏ hàng");
        }
    }

    @Override
    public ServiceResponse addCartLocalstorage(CartRequestNoLoginDTO dto) {
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");
            // tìm user có cart chưa nếu chưa có thì tạo mới
            // status -1 đã có giỏ hàng
            // status != -1 chưa có
            // == null chưa có
            BillEntity billEntity = billRepo.findByUserIdAndStatus(userId, BILL_CART).orElse(null);
            if (billEntity == null) {
                BillEntity billEntityBuild = BillEntity.builder()
                        .code(CodeUtil.generateCode())
                        .userId(userId)
                        .status(BILL_CART)
                        .totalPrice(BigDecimal.valueOf(0))
                        .build();
                billEntity = billRepo.save(billEntityBuild);
            }


            for (CartRequestDTO e : dto.getListProduct()) {
                ProductEntity product = productRepo.findByIdAndActive(e.getProductId(), ON).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("sản phẩm không tồn tại");
                // check product exist cart chua
                ProductBillEntity productBillCurrent = productBillRepo.findByProductIdAndBillId(product.getId(), billEntity.getId()).orElse(null);

                // tính tiền
                BigDecimal totalPriceProductBill = product.getPriceSell().multiply(BigDecimal.valueOf(e.getQuantity()));
                if (productBillCurrent == null) {
                    productBillCurrent = ProductBillEntity.builder()
                            .productId(product.getId())
                            .billId(billEntity.getId())
                            .priceSell(product.getPriceSell())
                            .quantity(e.getQuantity())
                            .totalPrice(totalPriceProductBill)
                            .build();
                    productBillRepo.save(productBillCurrent);
                } else {
                    // check trường hợp quantity quá số lượng
                    int quantityBill = productBillCurrent.getQuantity() + e.getQuantity();
                    if (quantityBill > product.getQuantity()) {
                        productBillCurrent.setQuantity(product.getQuantity());
                        // tính lại giá tính
                        totalPriceProductBill = product.getPriceSell().multiply(BigDecimal.valueOf(product.getQuantity()));
                        productBillCurrent.setTotalPrice(totalPriceProductBill);
                    } else {
                        productBillCurrent.setQuantity(quantityBill);
                        productBillCurrent.setTotalPrice(productBillCurrent.getTotalPrice().add(totalPriceProductBill));
                    }
                    productBillRepo.save(productBillCurrent);
                }
            }
            // get all total price
            List<BigDecimal> totalPriceList = productBillRepo.getAllTotalPrice(billEntity.getId());

            // công tổng bill
            BigDecimal totalBill = totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            billEntity.setTotalPrice(totalBill);
            billRepo.save(billEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Đồng bộ giỏ hàng  thành công");
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Lỗi thêm sản phẩm từ localStorage vào cart");
        }
    }

    @Override
    public ServiceResponse addCart(CartRequestDTO dto) {
        try {
            Long userId = getUserId();

            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            // tìm user có cart chưa nếu chưa có thì tạo mới
            // status -1 đã có giỏ hàng
            // status != -1 chưa có
            // == null chưa có
            BillEntity billEntity = billRepo.findByUserIdAndStatus(userId, BILL_CART).orElse(null);
            if (billEntity == null) {
                BillEntity billEntityBuild = BillEntity.builder()
                        .code(CodeUtil.generateCode())
                        .userId(userId)
                        .status(BILL_CART)
                        .totalPrice(BigDecimal.valueOf(0))
                        .build();
                billEntity = billRepo.save(billEntityBuild);
            }

            // Tìm sản phẩm
            ProductEntity product = productRepo.findByIdAndActive(dto.getProductId(), ON).orElse(null);
            if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");

            BigDecimal totalPriceProductBill = product.getPriceSell().multiply(BigDecimal.valueOf(dto.getQuantity()));

            // check product exist cart chua
            ProductBillEntity productBillCurrent = productBillRepo.findByProductIdAndBillId(dto.getProductId(), billEntity.getId()).orElse(null);

            // chua thi tim moi
            ProductBillEntity productBillEntity = null;
            if (productBillCurrent == null) {
                // tạo cart của user
                productBillEntity = ProductBillEntity.builder()
                        .productId(product.getId())
                        .billId(billEntity.getId())
                        .quantity(dto.getQuantity())
                        .priceSell(product.getPriceSell())
                        .totalPrice(totalPriceProductBill)
                        .build();
                productBillRepo.save(productBillEntity);
            } else {
                // check trường hợp quantity quá số lượng
                int quantityBill = productBillCurrent.getQuantity() + dto.getQuantity();
                if (quantityBill > product.getQuantity()) {
                    productBillCurrent.setQuantity(product.getQuantity());
                    // tính lại giá tính
                    totalPriceProductBill = product.getPriceSell().multiply(BigDecimal.valueOf(product.getQuantity()));
                    productBillCurrent.setTotalPrice(totalPriceProductBill);
                } else {
                    productBillCurrent.setQuantity(quantityBill);
                    productBillCurrent.setTotalPrice(productBillCurrent.getTotalPrice().add(totalPriceProductBill));
                }

                productBillRepo.save(productBillCurrent);
            }


            // get all total price
            List<BigDecimal> totalPriceList = productBillRepo.getAllTotalPrice(billEntity.getId());

            // công tổng bill
            BigDecimal totalBill = totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            billEntity.setTotalPrice(totalBill);
            billRepo.save(billEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Thêm vào giỏ hàng thành công");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi thêm sản phẩm vào giỏ hàng");
            return ServiceResponse.RESPONSE_ERROR("Thêm vào giỏ hàng thất bại");
        }
    }

    @Override
    public ServiceResponse changQuantity(CartRequestDTO dto) {
        try {
            ProductBillEntity productBillEntity = productBillRepo.findByProductIdAndBillId(dto.getProductId(), dto.getBillId()).orElse(null);
            if (productBillEntity == null)
                return ServiceResponse.RESPONSE_ERROR("Sản phẩm trang giỏ hàng không tồn tại");

            productBillEntity.setTotalPrice(productBillEntity.getPriceSell().multiply(BigDecimal.valueOf(dto.getQuantity())));
            productBillEntity.setQuantity(dto.getQuantity());

            productBillRepo.save(productBillEntity);
            // change giá tiền
            BillEntity billEntity = billRepo.findById(dto.getBillId()).orElse(null);
            if (productBillEntity == null)
                return ServiceResponse.RESPONSE_ERROR("Giỏ hàng không tồn tại");
            // get all total price
            List<BigDecimal> totalPriceList = productBillRepo.getAllTotalPrice(billEntity.getId());

            // công tổng bill
            BigDecimal totalBill = totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            billEntity.setTotalPrice(totalBill);
            billRepo.save(billEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi thay đổi số lượng sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi thay đổi số lượng sản phẩm");
        }
    }

    @Override
    public ServiceResponse deleteProductOfCart(CartRequestDTO dto) {
        try {
            ProductBillEntity productBillEntity = productBillRepo.findByProductIdAndBillId(dto.getProductId(), dto.getBillId()).orElse(null);
            if (productBillEntity == null)
                return ServiceResponse.RESPONSE_ERROR("Sản phẩm trang giỏ hàng không tồn tại");

            productBillRepo.delete(productBillEntity);
            // change giá tiền
            BillEntity billEntity = billRepo.findById(dto.getBillId()).orElse(null);
            if (productBillEntity == null)
                return ServiceResponse.RESPONSE_ERROR("Giỏ hàng không tồn tại");
            // get all total price
            List<BigDecimal> totalPriceList = productBillRepo.getAllTotalPrice(billEntity.getId());

            // công tổng bill
            BigDecimal totalBill = totalPriceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            billEntity.setTotalPrice(totalBill);
            billRepo.save(billEntity);
            return ServiceResponse.RESPONSE_SUCCESS("Thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi xóa sản phẩm trong giỏ hàng");
            return ServiceResponse.RESPONSE_ERROR("Lỗi xóa sản phẩm trong giỏ hàng");
        }
    }

    @Override
    public ServiceResponse totalCart() {
        try {
            Long userId = getUserId();

            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            BillEntity billEntity = billRepo.findByUserIdAndStatus(userId, BILL_CART).orElse(null);

            if (billEntity == null) return ServiceResponse.RESPONSE_SUCCESS(0);
            Integer totalItem = 0;
//            Integer totalItem = productBillRepo.countAllByBillId(billEntity.getId());
            List<ProductBillEntity> productBillEntities = productBillRepo.findAllByBillId(billEntity.getId());

            for (ProductBillEntity pbe : productBillEntities) {
                ProductEntity productEntity = productRepo.findByIdAndActive(pbe.getProductId(), USER_ACTIVE).orElse(null);
                if (productEntity != null) {
                    totalItem += pbe.getQuantity();
                }
            }

            return ServiceResponse.RESPONSE_SUCCESS(totalItem == null ? 0 : totalItem);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi thêm sản phẩm vào giỏ hàng");
            return ServiceResponse.RESPONSE_ERROR("Thêm vào giỏ hàng thất bại");
        }
    }

    @Override
    public ServiceResponse bill(BillUserRequestDTO dto) {
        try {
            Long userId = getUserId();

            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            BillEntity billEntity = billRepo.findByUserIdAndStatus(userId, BILL_CART).orElse(null);
            List<ProductBillEntity> billEntityList = productBillRepo.findByBillId(billEntity.getId());
            if (billEntity == null || billEntityList.size() == 0)
                return ServiceResponse.RESPONSE_ERROR("Vui lòng chọn các sản phẩm mà bạn muốn mua trước khi đặt hàng");
            billEntity.setBillDate(LocalDateTime.now());
            billEntity.setShippingCost(dto.getShippingCost());
            billEntity.setInvoiceValue(dto.getInvoiceValue());
            billEntity.setStatus(BILL_CONFIRM);
            billEntity.setNote(dto.getNote());
//            billEntity.setShip(billEntity.getTotalPrice().compareTo(BigDecimal.valueOf(700000)) <= 0 ? BigDecimal.valueOf(30000) : BigDecimal.valueOf(0));
            billEntity.setPayment(dto.getPay() == null ? 1 : dto.getPay());

            billEntity.setAddress(dto.getAddressDetail());

            UserEntity user = userRepo.findById(userId).orElse(null);
            if (user == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");
            user.setAddress(dto.getAddress());
            user.setWard(dto.getWard());
            user.setDistrict(dto.getDistrict());
            user.setCity(dto.getCity());


            List<ProductBillEntity> productBillEntities = productBillRepo.findByBillId(billEntity.getId());

            List<ProductEntity> productEntities = new ArrayList<>();

            for (ProductBillEntity e : productBillEntities) {
                ProductEntity product = productRepo.findByIdAndActive(e.getProductId(), ON).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                product.setQuantity(product.getQuantity() - e.getQuantity());
                product.setSold(product.getSold() + e.getQuantity());
                productEntities.add(product);
            }
            billRepo.save(billEntity);
            userRepo.save(user);
            productRepo.saveAll(productEntities);


            return ServiceResponse.RESPONSE_SUCCESS(billEntity.getId());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Đặt hàng thất bại");
            return ServiceResponse.RESPONSE_ERROR("Đặt hàng thất bại");
        }
    }

    @Override
    public ServiceResponse getBillDetailByIdUser(Long id) {
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            // tim cart
            BillEntity bill = billRepo.findById(id).orElse(null);
            if (bill == null) return ServiceResponse.RESPONSE_SUCCESS("Chưa có sản phẩm trong giỏ hàng");

            List<ProductBillEntity> productBillEntities = productBillRepo.findByBillId(bill.getId());

            BillResponseDto billResponseDto = new BillResponseDto();
            billResponseDto.setBillCode(bill.getCode());
            billResponseDto.setBillDate(bill.getBillDate());
            billResponseDto.setDeliveryTime(bill.getDeliveryTime());
            billResponseDto.setStatus(bill.getStatus());
            billResponseDto.setTotalPrice(bill.getTotalPrice());
            billResponseDto.setBillId(bill.getId());
//            billResponseDto.setShip(bill.getShip());

            List<BillDetailDto> listDetail = new ArrayList<>();
            for (ProductBillEntity pbe : productBillEntities) {

                ProductEntity product = productRepo.findById(pbe.getProductId()).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                List<Long> speId = productSpecialityRepo.getByProductId(product.getId());

                List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

                Collections.sort(speciality);

                BillDetailDto billDetailDto = BillDetailDto.builder()
                        .productId(pbe.getProductId())
                        .productName(product.getParentId() == null ? product.getProductName() : productRepo.findById(product.getParentId()).get().getProductName())
                        .priceSell(product.getPriceSell())
                        .quantity(pbe.getQuantity())
                        .totalPrice(pbe.getTotalPrice())
                        .img(product.getImg())
                        .speciality(speciality)
                        .build();
                listDetail.add(billDetailDto);

            }
            billResponseDto.setListDetail(listDetail);

            return ServiceResponse.RESPONSE_SUCCESS(billResponseDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra sản phẩm trong giỏ hàng");
        }
    }

    @Override
    public ServiceResponse getBillPayNow(Long productId, Integer quantity) {
        try {
            ProductEntity product = productRepo.findById(productId)
                    .orElseThrow(() -> new NullPointerException("Product do not exist"));

            if (product.getQuantity() < quantity)
                return ServiceResponse.RESPONSE_ERROR("Số lượng sản phẩm không đủ");
            List<Long> speId = productSpecialityRepo.getByProductId(product.getId());

            List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

            Collections.sort(speciality);

            CartResponseDto result = CartResponseDto.builder()
                    .id(product.getId())
                    .parentId(product.getParentId())
                    .productName(product.getProductName())
                    .img(product.getImg())
                    .quantity(quantity)
                    .priceSell(product.getPriceSell())
                    .totalQuantity(product.getQuantity())
                    .totalPrice(product.getPriceSell().multiply(BigDecimal.valueOf(quantity)))
                    .speciality(speciality)
                    .build();

            if (product.getParentId() != null) {
                ProductEntity parentProduct = productRepo.findById(product.getParentId())
                        .orElseThrow(() -> new NullPointerException("Product do not exist"));
                result.setParentName(parentProduct.getProductName());
            }
            return ServiceResponse.RESPONSE_SUCCESS(Arrays.asList(result));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi hiển thị đơn đặt mua ngay");
            return ServiceResponse.RESPONSE_ERROR("Lỗi hiển thị đơn đặt mua ngay");
        }
    }

    @Override
    public ServiceResponse addBillPayNow(BillUserRequestDTO dto) {
        try {
            Long userId = getUserId();

            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            UserEntity user = userRepo.findById(userId).orElseThrow(() -> new NullPointerException("User không tồn tại"));

            user.setAddress(dto.getAddress());
            user.setWard(dto.getWard());
            user.setDistrict(dto.getDistrict());
            user.setCity(dto.getCity());
            user.setCreatedDate(LocalDateTime.now());

            // tim san pham thep id
            ProductEntity product = productRepo.findById(dto.getProductId())
                    .orElseThrow(() -> new NullPointerException("Product do not exist"));
            product.setQuantity(product.getQuantity() < dto.getQuantity() ? 0 : product.getQuantity() - dto.getQuantity());
            product.setSold(product.getSold() + dto.getQuantity());


            // create bill new

            BigDecimal totalPrice = product.getPriceSell().multiply(BigDecimal.valueOf(dto.getQuantity()));
//            BigDecimal ship = totalPrice.compareTo(BigDecimal.valueOf(500000)) <= 0 ? BigDecimal.valueOf(30000) : BigDecimal.valueOf(0);
            BillEntity billEntity = BillEntity.builder()
                    .code(CodeUtil.generateCode())
                    .userId(user.getId())
//                    .ship(ship)
                    .totalPrice(totalPrice)
                    .shippingCost(dto.getShippingCost())
                    .invoiceValue(dto.getInvoiceValue())
                    .status(BILL_CONFIRM)
                    .address(dto.getAddressDetail())
                    .note(dto.getNote())
                    .payment(dto.getPay())
                    .billDate(LocalDateTime.now())
                    .build();

            userRepo.save(user);
            Long billId = billRepo.save(billEntity).getId();

            // create bill detail new
            ProductBillEntity productBillEntity = ProductBillEntity.builder()
                    .billId(billId)
                    .productId(product.getId())
                    .priceSell(product.getPriceSell())
                    .quantity(dto.getQuantity())
                    .totalPrice(totalPrice)
                    .status(0)
                    .build();

            productBillRepo.save(productBillEntity);

            productRepo.save(product);

            return ServiceResponse.RESPONSE_SUCCESS("Đặt hàng thành công");

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi đơn đặt");
            return ServiceResponse.RESPONSE_ERROR("Lỗi đơn đặt");
        }
    }

    /*
     * status = 0 get all bill
     *  = 1 xác nhận
     *  = 2 chờ đóng hàngs
     *  = 3 đang chuyển hàng
     *  = 4 đang giao
     *  = 5 thành công
     *  = 6 đã hủy
     * */
    @Override
    public ServiceResponse getAllByStatus(int status, int page, int limit) {
        List<BillResponseDto> result = new ArrayList<>();
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");
            List<BillEntity> billEntityList = new ArrayList<>();

            page = page > 0 ? page - 1 : 0;

            Pageable pageable = PageRequest.of(page, limit, Sort.by("billDate").descending());
            if (status == 0) {
                billEntityList = billRepo.findAllByUserIdAndStatusNotOrderByBillDateDesc(userId, BILL_CART, pageable);
            } else if (status == 1) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, BILL_CONFIRM, pageable);
            } else if (status == 2) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, BILL_CLOSE_PRODUCT, pageable);
            } else if (status == 3) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, BILL_TRANSPORTED, pageable);
            } else if (status == 4) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, BILL_DELIVERY, pageable);
            } else if (status == 5) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, BILL_SUCCESS, pageable);
            } else if (status == 6) {
                billEntityList = billRepo.findAllByUserIdAndStatusOrderByBillDateDesc(userId, INCOMPLETE, pageable);
            }

            for (BillEntity be : billEntityList) {
                List<ProductBillEntity> productBillEntities = productBillRepo.findByBillId(be.getId());
                // trả về
                BillResponseDto billResponseDto = new BillResponseDto();
                billResponseDto.setBillCode(be.getCode());
                billResponseDto.setBillDate(be.getBillDate());
                billResponseDto.setDeliveryTime(be.getDeliveryTime());
                billResponseDto.setStatus(be.getStatus());
                billResponseDto.setTotalPrice(be.getTotalPrice());
                billResponseDto.setShippingCost(be.getShippingCost());
                billResponseDto.setInvoiceValue(be.getInvoiceValue());
                billResponseDto.setBillId(be.getId());
//                billResponseDto.setShip(be.getShip());
                ///
                List<BillDetailDto> listDetail = new ArrayList<>();
                for (ProductBillEntity pbe : productBillEntities) {

                    ProductEntity product = productRepo.findById(pbe.getProductId()).orElse(null);
                    if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                    List<Long> speId = productSpecialityRepo.getByProductId(product.getId());

                    List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

                    Collections.sort(speciality);

                    BillDetailDto billDetailDto = BillDetailDto.builder()
                            .parentId(product.getParentId())
                            .productId(product.getId())
                            .productName(product.getParentId() == null ? product.getProductName() : productRepo.findById(product.getParentId()).get().getProductName())
                            .priceSell(product.getPriceSell())
                            .quantity(pbe.getQuantity())
                            .totalPrice(pbe.getTotalPrice())
                            .img(product.getImg())
                            .speciality(speciality)
                            .review(pbe.getStatus() == null ? 0 : pbe.getStatus())
                            .build();
                    listDetail.add(billDetailDto);

                }
                billResponseDto.setListDetail(listDetail);
                result.add(billResponseDto);
            }
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Lỗi gửi all bill");
            return ServiceResponse.RESPONSE_ERROR(result);
        }
    }

    @Override
    public ServiceResponse totalByStatus(int status) {
        try {
            Long userId = getUserId();
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");
            int total = 1;
            if (status == 0) {
                total = billRepo.countAllByUserId(userId);
            } else if (status == 1) {
                total = billRepo.countAllByUserIdAndStatus(userId, BILL_CONFIRM);
            } else if (status == 2) {
                total = billRepo.countAllByUserIdAndStatus(userId, BILL_CLOSE_PRODUCT);
            } else if (status == 3) {
                total = billRepo.countAllByUserIdAndStatus(userId, BILL_TRANSPORTED);
            } else if (status == 4) {
                total = billRepo.countAllByUserIdAndStatus(userId, BILL_DELIVERY);
            } else if (status == 5) {
                total = billRepo.countAllByUserIdAndStatus(userId, BILL_SUCCESS);
            } else if (status == 6) {
                total = billRepo.countAllByUserIdAndStatus(userId, INCOMPLETE);
            }
            int limit = 4;

            int result = (int) Math.ceil(((float) total) / limit);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (
                Exception e) {
            e.printStackTrace();
            log.error("Lỗi lấy tổng page");
            return ServiceResponse.RESPONSE_SUCCESS(1);
        }

    }


    // admin
    @Override
    public ServiceResponse getAllAdmin() {
        try {

            List<BillEntity> entities = billRepo.findAllByStatusNotOrderByBillDateDesc(BILL_CART);
            List<BillAdminResponseDto> result = new ArrayList<>();

            for (BillEntity e : entities) {

                String userName = userRepo.findById(e.getUserId()).get().getFullName();

                String staffName = "";
                if (e.getStaffId() != null)
                    staffName = userRepo.findById(e.getStaffId()).get().getFullName();

                BillAdminResponseDto dto = BillAdminResponseDto.builder()
                        .id(e.getId())
                        .code(e.getCode())
                        .billDate(e.getBillDate())
                        .deliveryTime(e.getDeliveryTime())
//                        .ship(e.getShip())
                        .totalPrice(e.getTotalPrice())
                        .note(e.getNote())
                        .status(e.getStatus())
                        .payment(e.getPayment())
                        .userId(userName)
                        .staffId(staffName)
                        .checkBill(e.getStatus() == 1 ? true : false)
                        .build();
                result.add(dto);

            }

            return ServiceResponse.RESPONSE_SUCCESS("success", result);
        } catch (Exception e) {
            log.error("error get all bill");
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("error get all bill");
        }
    }

    @Override
    public ServiceResponse getBillDetailById(Long id) {
        try {
            BillResponseDto result = new BillResponseDto();

            BillEntity re = billRepo.findById(id).orElse(null);
            if (re == null) return ServiceResponse.RESPONSE_ERROR("error receipt does exist");

            result.setBillCode(re.getCode());
            result.setBillDate(re.getBillDate());
            result.setDeliveryTime(re.getDeliveryTime());
            result.setTotalPrice(re.getTotalPrice());
            result.setShippingCost(re.getShippingCost());
            result.setInvoiceValue(re.getInvoiceValue());
            result.setStatus(re.getStatus());
            result.setNote(re.getNote());
//            result.setShip(re.getShip());
            result.setAddress(re.getAddress());
            result.setPayment(re.getPayment() == null ? 1 : re.getPayment());

            // người dùng
            UserEntity customer = userRepo.findById(re.getUserId()).orElseThrow(() -> new NullPointerException("User do not exist"));
            result.setUserName(customer.getUserName());
            result.setFullName(customer.getFullName());
            result.setEmail(customer.getEmail());
            result.setPhone(customer.getPhone());

            if (re.getStaffId() != null)
                result.setStaffName(userRepo.findById(re.getStaffId()).get().getFullName());


            List<ProductBillEntity> productBillEntities = productBillRepo.findAllByBillId(id);

            List<BillDetailDto> listDetail = new ArrayList<>();
            if (productBillEntities.size() > 0) {
                for (ProductBillEntity e : productBillEntities) {
                    BillDetailDto dto = BillDetailDto.builder()
                            .quantity(e.getQuantity())
                            .totalPrice(e.getTotalPrice())
                            .priceSell(e.getPriceSell()).build();
                    ProductEntity productEntity = productRepo.findById(e.getProductId()).orElse(null);

                    List<Long> speId = productSpecialityRepo.getByProductId(productEntity.getId());

                    List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);

                    Collections.sort(speciality);


                    if (productEntity == null) return ServiceResponse.RESPONSE_ERROR("error receipt does exist");
                    dto.setProductCode(productEntity.getProductCode());
                    dto.setImg(productEntity.getImg());
                    if (productEntity.getParentId() == null) {
                        dto.setProductName(productEntity.getProductName() + "(Size " + speciality.get(0).getDescription() + " - Màu " + speciality.get(1).getDescription() + ")");
                    } else {
                        dto.setProductName(productEntity.getProductName());
                    }

                    listDetail.add(dto);
                }
            }

            result.setListDetail(listDetail);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get detail receipt");

            return ServiceResponse.RESPONSE_ERROR("error receipt get detail");
        }
    }

    // verify bill
    @Override
    public ServiceResponse verifyBill(Long id) {
        try {
            BillEntity billEntity = billRepo.findById(id).orElse(null);
            if (billEntity == null) return ServiceResponse.RESPONSE_ERROR("Đơn đặt không tồn tại");

            billEntity.setStatus(BILL_CLOSE_PRODUCT);
            // user confirm
            Long userId = getUserId();

            billEntity.setStaffId(userId);

            // verify email
            BillResponseDto dto = (BillResponseDto) getBillDetailById(id).getData();
            sendEmailService.sendMail(dto);
            // verify email


            billRepo.save(billEntity);

            UserEntity user = userRepo.findById(userId).orElse(null);
            if (user == null) return ServiceResponse.RESPONSE_ERROR("Nhân viên không tồn tại");


            return ServiceResponse.RESPONSE_SUCCESS("Xác minh đơn đặt hàng mã " + billEntity.getCode() + " thành công", user.getFullName());
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Xác minh đơn đặt hàng thất bại");
        }
    }

    @Override
    public ServiceResponse cancelBill(Long id) {
        try {
            BillEntity billEntity = billRepo.findByIdAndStatusNot(id, BILL_SUCCESS).orElse(null);
            if (billEntity == null) return ServiceResponse.RESPONSE_ERROR("Đơn đặt không tồn tại");

            billEntity.setStatus(INCOMPLETE);

            List<ProductBillEntity> productBillEntities = productBillRepo.findByBillId(billEntity.getId());

            List<ProductEntity> productEntities = new ArrayList<>();

            for (ProductBillEntity e : productBillEntities) {
                ProductEntity product = productRepo.findByIdAndActive(e.getProductId(), ON).orElse(null);
                if (product == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                product.setQuantity(product.getQuantity() + e.getQuantity());
                product.setSold(product.getSold() - e.getQuantity());
                productEntities.add(product);
            }
            productRepo.saveAll(productEntities);

            // user cancel
            Long userId = getUserId();
            UserRole userrole = userRoleRepo.findByUserId(userId);
            if(userrole.getRoleId() == 4 || userrole.getRoleId() == 2) {
                billEntity.setStaffId(userId);
            }
//            billEntity.setStaffId(userId); BNM,
            billRepo.save(billEntity);

            UserEntity user = userRepo.findById(userId).orElse(null);
            if (user == null) return ServiceResponse.RESPONSE_ERROR("Nhân viên không tồn tại");


            return ServiceResponse.RESPONSE_SUCCESS("Hủy đơn đặt hàng mã " + billEntity.getCode() + " thành công", user.getFullName());
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Hủy đơn đặt hàng thất bại");
        }
    }

    @Override
    public ServiceResponse updateStatusBill(Long billId) {
        try {
            BillEntity billEntity = billRepo.findById(billId).orElse(null);
            if (billEntity == null) return ServiceResponse.RESPONSE_ERROR("Đơn hàng không tồn tại");

            if (billEntity.getStatus() == 5 || billEntity.getStatus() == -2)
                return ServiceResponse.RESPONSE_ERROR("Không thể thay đổi trạng thái đơn hàng đã thành công");
            billEntity.setStatus(billEntity.getStatus() + 1);
            if (billEntity.getStatus() == 5) {
                billEntity.setDeliveryTime(LocalDateTime.now());
                List<ProductBillEntity> billEntityList = productBillRepo.findByBillId(billEntity.getId());
                billEntityList.forEach(e -> {
                    e.setStatus(0);
                });
                productBillRepo.saveAll(billEntityList);
            }

            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi trạng thái đơn đặt hàng " + billEntity.getCode() + " thành công", billRepo.save(billEntity));
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Thay đổi trạng thái đơn đặt hàng thất bại");
        }
    }

    //admin

    private Long getUserId() {
        Long userId = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            userId = userRepo.findByUserName(currentUserName).get().getId();
        }
        return userId;
    }
}
