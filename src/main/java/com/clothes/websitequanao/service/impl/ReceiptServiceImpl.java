package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.receipt.ReceiptRequestDto;
import com.clothes.websitequanao.dto.request.receipt.UpdaterReceiptRequest;
import com.clothes.websitequanao.dto.response.ReceiptAdminResponseDto;
import com.clothes.websitequanao.dto.request.receipt.ProductReceiptRequestDto;
import com.clothes.websitequanao.dto.response.receipt.ProductReceiptDto;
import com.clothes.websitequanao.dto.response.receipt.ReceiptDetailDto;
import com.clothes.websitequanao.dto.response.receipt.ReceiptDto;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.entity.ProductReceiptEntity;
import com.clothes.websitequanao.entity.ReceiptEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.ProductReceiptRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.repository.ReceiptRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.service.ReceiptService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.FunctionStatus.ON;
import static com.clothes.websitequanao.common.Consts.ReceiptType.*;
import static com.clothes.websitequanao.utils.CodeUtil.generateCode;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ReceiptRepo receiptRepo;

    private final UserRepo userRepo;

    private final ProductReceiptRepo productReceiptRepo;

    private final ProductRepo productRepo;

    @Override
    public ServiceResponse getAll() {
        try {

            List<ReceiptEntity> entities = receiptRepo.findAllByOrderByReceiptDateDesc();
            List<ReceiptAdminResponseDto> result = new ArrayList<>();

            for (ReceiptEntity e : entities) {

                String staffName = userRepo.findById(e.getUserId()).get().getFullName();


                String supplierName = "";
                if (e.getSupplierId() != null)
                    supplierName = userRepo.findById(e.getSupplierId()).get().getFullName();

                ReceiptAdminResponseDto dto = ReceiptAdminResponseDto.builder()
                        .id(e.getId())
                        .code(e.getCode())
                        .receiptDate(e.getReceiptDate())
                        .receivedDate(e.getReceivedDate())
                        .totalPrice(e.getTotalPrice())
                        .note(e.getNote())
                        .status(e.getStatus())
                        .userId(staffName)
                        .supplierId(supplierName)
                        .checkChooseManu(e.getStatus() == -2 ? true : false)
                        .supplierIdChange(e.getSupplierId() == null ? 0 : e.getSupplierId())
                        .build();
                result.add(dto);

            }

            return ServiceResponse.RESPONSE_SUCCESS("success", result);
        } catch (Exception e) {
            log.error("error get all receipt");
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("error get all receipt");
        }
    }

    @Override
    public ServiceResponse getReceiptById(Long id) {
        try {
            ReceiptDto result = new ReceiptDto();

            ReceiptEntity re = receiptRepo.findById(id).orElse(null);
            if (re == null) return ServiceResponse.RESPONSE_ERROR("error receipt does exist");

            result.setReceiptCode(re.getCode());
            result.setReceiptDate(re.getReceiptDate());
            result.setReceivedDate(re.getReceivedDate());
            result.setTotalPrice(re.getTotalPrice());
            result.setStatus(re.getStatus());
            result.setNote(re.getNote());
            result.setStaffName(userRepo.findById(re.getUserId()).get().getFullName());
            if (re.getSupplierId() != null)
                result.setSupplierName(userRepo.findById(re.getSupplierId()).get().getFullName());


            List<ProductReceiptEntity> productReceiptEntities = productReceiptRepo.findAllByReceiptId(id);

            List<ReceiptDetailDto> listDetail = new ArrayList<>();
            if (productReceiptEntities.size() > 0) {
                for (ProductReceiptEntity e : productReceiptEntities) {
                    ReceiptDetailDto dto = ReceiptDetailDto.builder()
                            .quantity(e.getQuantity())
                            .totalPrice(e.getTotalPrice())
                            .priceEntry(e.getPriceEntry()).build();
                    ProductEntity productEntity = productRepo.findById(e.getProductId()).orElse(null);
                    if (productEntity == null) return ServiceResponse.RESPONSE_ERROR("error receipt does exist");
                    dto.setProductCode(productEntity.getProductCode());
                    dto.setProductName(productEntity.getProductName());
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

    @Override
    public ServiceResponse getAllProduct() {

        try {
            List<ProductReceiptDto> result = new ArrayList<>();

            List<ProductEntity> productEntities = productRepo.findAllByParentIdAndActive(null, ON);


            for (ProductEntity e : productEntities) {
                // product parent
                ProductReceiptDto productReceiptDto = convertEntity(e);
                if (productReceiptDto.getParentId() == null) {
                    // product child entity
                    List<ProductEntity> productChild = productRepo.findAllByParentIdAndActive(productReceiptDto.getId(), ON);
                    // product child dto
                    List<ProductReceiptDto> productReceiptDtoChild = new ArrayList<>();
                    // covert entity -> dto
                    if (productChild.size() > 0) {
                        productChild.forEach(child -> {
                            productReceiptDtoChild.add(convertEntity(child));
                        });

                    }
                    productReceiptDto.setCheckChildren(productReceiptDtoChild.size() > 0 ? true : false);
                    productReceiptDto.setProductChild(productReceiptDtoChild);
                }
                result.add(productReceiptDto);
            }

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error product receipt");
            return ServiceResponse.RESPONSE_ERROR("Lỗi hiển thị sản phẩm");
        }

    }

    // admin
    @Override
    public ServiceResponse addReceipt(ReceiptRequestDto dto) {
        try {
            // RECEIPT
            Long userId = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                userId = userRepo.findByUserName(currentUserName).get().getId();
            }

            ReceiptEntity receiptEntity = ReceiptEntity.builder()
                    .code(generateCode())
                    .supplierId(dto.getSupplierId())
                    .note(dto.getNote())
                    .userId(userId)
                    .receiptDate(LocalDateTime.now())
                    .build();
            // set status
            if (dto.getSupplierId() != null) {
                receiptEntity.setStatus(CONFIRM);
            } else {
                receiptEntity.setStatus(INCOMPLETE);
            }
            long receiptId = receiptRepo.save(receiptEntity).getId();
            //PRODUCT_RECEIPT
            List<BigDecimal> priceList = productReceiptBuilder(dto.getListProductSelect(), receiptId);
            // tổng của receipt
            BigDecimal totalPrice = priceList.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            // set total
            receiptEntity.setTotalPrice(totalPrice);
            // update lai phieu nhap kho
            receiptRepo.save(receiptEntity);

            return ServiceResponse.RESPONSE_SUCCESS("Thêm phiếu nhập kho thành công");
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Thêm phiếu nhập kho thất bại");
        }

    }

    @Override
    public ServiceResponse updateUserManu(UpdaterReceiptRequest dto) {
        try {
            ReceiptEntity receiptEntity = receiptRepo.findById(dto.getId()).orElse(null);
            if (receiptEntity == null) return ServiceResponse.RESPONSE_ERROR("Phiếu nhập kho không tồn tại");

            receiptEntity.setSupplierId(dto.getSupplierId());
            receiptEntity.setStatus(CONFIRM);
            receiptRepo.save(receiptEntity);

            return ServiceResponse.RESPONSE_SUCCESS("Hoàn thành phiếu nhập kho thành công");
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Hoàn thành phiếu nhập kho thất bại");
        }
    }

    @Override
    public ServiceResponse updateStatusReceipt(Long receiptId) {
        try {
            ReceiptEntity receiptEntity = receiptRepo.findById(receiptId).orElse(null);
            if (receiptEntity == null) return ServiceResponse.RESPONSE_ERROR("Phiếu nhập kho không tồn tại");

            if (receiptEntity.getStatus() == 3)
                return ServiceResponse.RESPONSE_ERROR("Không thể thay đổi trạng thái đơn hàng");
            receiptEntity.setStatus(receiptEntity.getStatus() + 1);
            if (receiptEntity.getStatus() == 3) {
                receiptEntity.setReceivedDate(LocalDateTime.now());
                List<ProductReceiptEntity> productReceiptEntities = productReceiptRepo.findAllByReceiptId(receiptEntity.getId());

                List<ProductEntity> productEntityList = new ArrayList<>();
                productReceiptEntities.forEach(e -> {
                    ProductEntity productEntity = productRepo.findById(e.getProductId()).orElse(null);
                    productEntity.setQuantity(productEntity.getQuantity() + e.getQuantity());
                    productEntityList.add(productEntity);
                });

                if (productEntityList.size() > 0) {
                    productRepo.saveAll(productEntityList);
                }
            }


            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi trạng thái phiếu nhập kho " + receiptEntity.getCode() + " thành công", receiptRepo.save(receiptEntity));
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Thay đổi trạng thái phiếu nhâp kho thất bại");
        }
    }

    @Override
    public ServiceResponse cancelReceipt(Long receiptId) {
        try {
            ReceiptEntity receiptEntity = receiptRepo.findById(receiptId).orElse(null);
            if (receiptEntity == null) return ServiceResponse.RESPONSE_ERROR("Phiếu nhập kho không tồn tại");
//            if (receiptEntity.getStatus() != CONFIRM)
//                return ServiceResponse.RESPONSE_ERROR("Hủy đơn phiếu nhập kho thất bại");
            receiptEntity.setStatus(CANCEL);
            return ServiceResponse.RESPONSE_SUCCESS("Hủy phiếu nhập kho " + receiptEntity.getCode() + " thành công", receiptRepo.save(receiptEntity));
        } catch (Exception ex) {
            return ServiceResponse.RESPONSE_ERROR("Hủy phiếu nhập kho thất bại");
        }
    }

    //ADD
    private List<BigDecimal> productReceiptBuilder(List<ProductReceiptRequestDto> dto, long receiptId) {
        List<ProductReceiptEntity> receiptDetail = new ArrayList<>();
        List<BigDecimal> result = new LinkedList<>();
        if (dto.size() > 0) {
            for (ProductReceiptRequestDto e : dto) {
                // get product
                ProductEntity productEntity = productRepo.findById(e.getId()).orElse(null);

                BigDecimal total = new BigDecimal(String.valueOf(BigDecimal.valueOf(e.getQuantitySelect()).multiply(productEntity.getPrice())));
                result.add(total);
                ProductReceiptEntity productReceiptEntity = ProductReceiptEntity.builder()
                        .productId(e.getId())
                        .quantity(e.getQuantitySelect())
                        .receiptId(receiptId)
                        .priceEntry(productEntity.getPrice())
                        .totalPrice(total)
                        .build();
                receiptDetail.add(productReceiptEntity);
            }
            productReceiptRepo.saveAll(receiptDetail);
        }
        return result;
    }
    //ADD

    private ProductReceiptDto convertEntity(ProductEntity entity) {
        ProductReceiptDto result = ProductReceiptDto.builder()
                .id(entity.getId())
                .checkSelect(false)
                .productName(entity.getProductName())
                .productCode(entity.getProductCode())
                .price(entity.getPrice())
                .priceSell(entity.getPriceSell())
                .img(entity.getImg())
                .parentId(entity.getParentId())
                .quantity(entity.getQuantity())
                .build();
        return result;
    }

}
