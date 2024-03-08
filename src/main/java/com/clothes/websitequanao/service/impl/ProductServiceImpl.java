package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.product.ProductAdminRequestDto;
import com.clothes.websitequanao.dto.request.product.ProductDetailRequestDto;
import com.clothes.websitequanao.dto.request.product.ProductRequestDto;
import com.clothes.websitequanao.dto.response.product.ProductDetailResponseDto;
import com.clothes.websitequanao.dto.response.product.ProductDetailSpeResponseDto;
import com.clothes.websitequanao.entity.*;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.*;
import com.clothes.websitequanao.service.FavoriteService;
import com.clothes.websitequanao.service.FileService;
import com.clothes.websitequanao.service.ProductService;
import com.clothes.websitequanao.utils.CodeUtil;
import com.clothes.websitequanao.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.FunctionStatus.ON;
import static com.clothes.websitequanao.common.Consts.productType.NORMAL;
import static com.clothes.websitequanao.common.Consts.specialityType.COLOR_TYPE;
import static com.clothes.websitequanao.common.Consts.specialityType.SIZE_TYPE;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;

    private final FileService fileService;

    private final ProductSpecialityRepo productSpecialityRepo;

    private final SpecialityRepo specialityRepo;

    private final ImgRepo imgRepo;

    private final CategoryRepo categoryRepo;

    private final BrandRepo brandRepo;

    private final FavoriteService favoriteService;

    private final EntityManager entityManager;

    private final FavoriteRepo favoriteRepo;

    private final RateRepo rateRepo;


    private static final String QUERY = "Select p.* from product p left join product_featured ps on p.id = ps.product_id";

    @Override
    public ServiceResponse getProductNew() {
        try {
            Pageable pageable = PageRequest.of(0, 8);
            List<ProductEntity> result = productRepo.findAllByParentIdAndActiveOrderByCreatedDateDesc(null, ON, pageable);
            favoriteService.setFavorite(result);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("add or edit error");
            return ServiceResponse.RESPONSE_ERROR("error get product home");
        }
    }

    @Override
    public ServiceResponse productOptionHome(Integer option) {
        try {
            String sqlQuery;
            if (option == 1) {
                sqlQuery = "SELECT p.* FROM product p WHERE parent_id IS NULL AND active = :active ORDER BY p.created_date DESC";
            } else if (option == 2) {
                sqlQuery = "SELECT p.*, COUNT(f.product_id) AS favorites FROM product p " +
                        "JOIN favorite f ON p.id = f.product_id WHERE p.parent_id IS NULL AND p.active = :active " +
                        "GROUP BY f.product_id ORDER BY favorites DESC";
            } else if (option == 3) {
                sqlQuery = "SELECT p.*, SUM(p.sold) AS total_sold, " +
                        "(CASE WHEN p.parent_id IS NULL THEN p.id ELSE p.parent_id END) AS product_sold " +
                        "FROM product p WHERE p.active = :active GROUP BY product_sold ORDER BY total_sold DESC";
            } else {
                sqlQuery = "SELECT p.* FROM product p " +
                        "WHERE parent_id IS NULL AND active = :active AND p.sale != 0 ORDER BY p.sale DESC";
            }
            sqlQuery += " LIMIT 0,8";

            Query query = entityManager.createNativeQuery(sqlQuery, ProductEntity.class);
            query.setParameter("active", ON);
            List<ProductEntity> result = query.getResultList();

            // Calculate total sold
            for (ProductEntity product : result) {
                Integer totalPay = productRepo.findAllByParentId(product.getId())
                        .stream()
                        .mapToInt(ProductEntity::getSold)
                        .sum();
                product.setTotalPay(totalPay);
            }

            favoriteService.setFavorite(result);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi lấy ra sản phẩm ở trang chủ");
            return ServiceResponse.RESPONSE_ERROR("lỗi lấy ra sản phẩm ở trang chủ");
        }
    }

    @Override
    public List<ProductEntity> getAllProduct(Long id) {
        return productRepo.findAllByParentId(id);
    }

    @Override
    public ServiceResponse addOrEditProduct(ProductAdminRequestDto dto) {
        try {

            if (dto.getId() == null) {
                boolean checkProductName = productRepo.existsAllByProductName(dto.getProductName());
                if (checkProductName) return ServiceResponse.RESPONSE_ERROR("Tên sản phẩm đã tồn tại, vui lòng kiểm tra lại");
                addProduct(dto);
                return ServiceResponse.RESPONSE_SUCCESS("Thêm sản phẩm thành công!");
            } else {
                Long productId = dto.getId();
                String productName = dto.getProductName();
                Optional<ProductEntity> existingProduct = productRepo.findById(productId);
                if (existingProduct.isPresent()) {
                    // Nếu tên sản phẩm đã thay đổi
                    if (!productName.equals(existingProduct.get().getProductName())) {
                        boolean checkProductName = productRepo.existsAllByProductNameAndIdNot(productName, productId);
                        if (checkProductName) {
                            return ServiceResponse.RESPONSE_ERROR("Tên sản phẩm đã tồn tại, vui lòng kiểm tra lại.");
                        }
                    }
                    editProduct(dto);
                    return ServiceResponse.RESPONSE_SUCCESS("Sửa sản phẩm thành công!");
                } else {
                    return ServiceResponse.RESPONSE_ERROR("Đã có lỗi xảy ra, vui lòng kiểm tra lại.");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            log.error("add or edit error");
            return ServiceResponse.RESPONSE_ERROR("error add or edit product");
        }
    }

    @Override
    public boolean checkExistChild(Long id) {
        boolean result = productRepo.existsByParentId(id);
        return result;
    }

    @Override
    public int getTotalQuantityOfChildren(Long parentId) {
        // Sử dụng productRepo để lấy danh sách các sản phẩm con của sản phẩm cha
        List<ProductEntity> children = productRepo.findAllByParentId(parentId);

        int totalQuantity = 0;
        for (ProductEntity child : children) {
            // Cộng dồn quantity của từng sản phẩm con
            totalQuantity += child.getQuantity();
        }

        return totalQuantity;
    }

    // thêm sản phẩm
    private void addProduct(ProductAdminRequestDto dto) {
        // để ảnh lên firebase
        String img = (String) fileService.upload(dto.getImg());
        long id; // dùng để lưu id sản phẩm vừa tạo
        if(dto.getParentId() == null) {
            ProductEntity productEntity = ProductEntity.builder()
                    .productName(dto.getProductName())
                    .price(dto.getPrice())
                    .priceSell(dto.getPriceSell())
                    .productCode(dto.getProductCode())
                    .description(dto.getDescription())
                    .content(dto.getContent())
                    .categoryId(dto.getCategoryId())
                    .brandId(dto.getBrandId())
                    .parentId(null)
                    .active(dto.getActive())
                    .img(img)
                    .type(NORMAL)
                    .quantity(0)
                    .sale(0)
                    .sold(0)
                    .rate(0f)
                    .totalRate(0)
                    .build();
            // Lấy về id sản phẩm vừa tạo để lưu img
            id = productRepo.save(productEntity).getId();
        } else {
            // Lấy thông tin từ sản phẩm cha
            Optional<ProductEntity> parentProductOptional = productRepo.findById(dto.getParentId());

            if (parentProductOptional.isPresent()) {
                ProductEntity parentProduct = parentProductOptional.get();

                // Sử dụng thông tin từ sản phẩm cha để tạo sản phẩm con
                ProductEntity productEntity = ProductEntity.builder()
                        .productName(dto.getProductName())
                        .price(dto.getPrice())
                        .priceSell(dto.getPriceSell())
                        .productCode(dto.getProductCode())
                        .description(dto.getDescription())
                        .content(dto.getContent())
                        .categoryId(parentProduct.getCategoryId())
                        .brandId(parentProduct.getBrandId())
                        .parentId(dto.getParentId())
                        .active(dto.getActive())
                        .img(img)
                        .type(NORMAL)
                        .quantity(0)
                        .sale(0)
                        .sold(0)
                        .rate(0f)
                        .totalRate(0)
                        .build();

                // Lấy về id sản phẩm vừa tạo để lưu img
                id = productRepo.save(productEntity).getId();

                // Các thao tác khác sau khi lưu sản phẩm

            } else {
                // Xử lý trường hợp không tìm thấy sản phẩm cha
                throw new RuntimeException("Không tìm thấy sản phẩm cha");
            }
        }

        List<ProductSpeciality> productSpecialities = new ArrayList<>();
        if (dto.getSpecialityId() != null && !dto.getSpecialityId().isEmpty()) {
            dto.getSpecialityId().forEach(e -> {
                ProductSpeciality productSpeciality = ProductSpeciality.builder()
                        .productId(id)
                        .featuredId(e).build();
                productSpecialities.add(productSpeciality);
            });
        }

        // add img other
        if (dto.getListImg() != null && !dto.getListImg().isEmpty()) {
            List<ImgEntity> imgs = new ArrayList<>();
            for (MultipartFile file : dto.getListImg()) {
                img = (String) fileService.upload(file);
                ImgEntity imgEntity = ImgEntity.builder()
                        .img(img)
                        .productId(id).build();
                imgs.add(imgEntity);

            }
            imgRepo.saveAll(imgs);
        }
        productSpecialityRepo.saveAll(productSpecialities);
    }

    // edit
    private void editProduct(ProductAdminRequestDto dto) {

        String img;
        // tìm product
        ProductEntity productCurrent = this.productRepo.findById(dto.getId()).orElse(null);
        if (productCurrent == null) throw new NullPointerException();

        if(dto.getParentId() == null) {
            productCurrent.setProductName(dto.getProductName());
//            productCurrent.setPrice(dto.getPrice());
//            productCurrent.setPriceSell(dto.getPriceSell());
//            productCurrent.setProductCode(dto.getProductCode());
            productCurrent.setDescription(dto.getDescription());
            productCurrent.setContent(dto.getContent());
            productCurrent.setBrandId(dto.getBrandId());
            productCurrent.setActive(dto.getActive());
            productCurrent.setType(NORMAL);

            // Đặt active cho child
            Long category = dto.getCategoryId();
            BigDecimal price = dto.getPrice();
            BigDecimal priceSell = dto.getPriceSell();
            String productCode = dto.getProductCode();
            List<ProductEntity> productChildren = productRepo.findAllByParentId(dto.getId());
            if (!productChildren.isEmpty()) {
//                log.info("Category: {}", dto.getBrandId());
//                log.info("Updated Product Children: {}", productCurrent.getCategoryId());
                if(productCode != null && !productCode.equals(productCurrent.getProductCode())) {
                    productCurrent.setProductCode(productCode);
                    // Cập nhật product code cho tất cả sản phẩm con
                    productChildren.forEach(e -> {
                        e.setProductCode(productCode);
                    });
                }
                if(price != null && !price.equals(productCurrent.getPrice())) {
                    productCurrent.setPrice(price);
                    // Cập nhật price cho tất cả sản phẩm con
                    productChildren.forEach(e -> {
                        e.setPrice(price);
                    });
                }
                if(priceSell != null && !priceSell.equals(productCurrent.getPriceSell())) {
                    productCurrent.setPriceSell(priceSell);
                    // Cập nhật price sell cho tất cả sản phẩm con
                    productChildren.forEach(e -> {
                        e.setPriceSell(priceSell);
                    });
                }
                if(category != null && !category.equals(productCurrent.getCategoryId())) {
                    productCurrent.setCategoryId(category);
                    // Cập nhật category cho tất cả sản phẩm con
                    productChildren.forEach(e -> {
                        e.setCategoryId(category);
                    });
                }
                productChildren.forEach(e -> {
                    e.setBrandId(dto.getBrandId());
                });
                productChildren.forEach(e -> {
                    e.setActive(dto.getActive());
                });
                productRepo.saveAll(productChildren);
            }
        } else {
            // Lấy thông tin từ sản phẩm cha
            Optional<ProductEntity> parentProductOptional = productRepo.findById(dto.getParentId());

            if (parentProductOptional.isPresent()) {
                ProductEntity parentProduct = parentProductOptional.get();

                productCurrent.setProductName(dto.getProductName());
                productCurrent.setPrice(dto.getPrice());
                productCurrent.setPriceSell(dto.getPriceSell());
                productCurrent.setProductCode(dto.getProductCode());
                productCurrent.setDescription(dto.getDescription());
                productCurrent.setContent(dto.getContent());
                productCurrent.setBrandId(parentProduct.getBrandId());
                productCurrent.setCategoryId(parentProduct.getCategoryId());
                productCurrent.setParentId(dto.getParentId());
                productCurrent.setActive(dto.getActive());
                productCurrent.setType(NORMAL);
            } else {
                // Xử lý trường hợp không tìm thấy sản phẩm cha
                throw new RuntimeException("Không tìm thấy sản phẩm cha");
            }
        }
        // check img có thay đổi không
        // nếu có xóa ảnh cũ ở firebase
        // add ảnh mới vào firebase
        if (dto.getImg() != null) {

            fileService.deleteFile(FileUtil.getImgName(productCurrent.getImg()));
//            // thêm ảnh
            img = (String) fileService.upload(dto.getImg());
            // save link database
            productCurrent.setImg(img);
        }
        productRepo.save(productCurrent);


        // xóa đặc trưng
        deleteProductSpecial(productCurrent.getId());
        // thêm lại đặc trung mới
        List<ProductSpeciality> productSpecialities = new ArrayList<>();
        if(dto.getSpecialityId() != null && !dto.getSpecialityId().isEmpty()) {
            dto.getSpecialityId().forEach(e -> {
                ProductSpeciality productSpeciality = ProductSpeciality.builder()
                        .productId(productCurrent.getId())
                        .featuredId(e).build();
                productSpecialities.add(productSpeciality);
            });
            productSpecialityRepo.saveAll(productSpecialities);
        }
        //

        if (dto.getListIdDelete() != null && !dto.getListIdDelete().isEmpty()) {
            imgRepo.deleteAllById(dto.getListIdDelete());
        }
        // add img other
        if (dto.getListImg() != null && !dto.getListImg().isEmpty()) {
            List<ImgEntity> imgs = new ArrayList<>();
            for (MultipartFile file : dto.getListImg()) {
                img = (String) fileService.upload(file);
                ImgEntity imgEntity = ImgEntity.builder()
                        .img(img)
                        .productId(productCurrent.getId()).build();
                imgs.add(imgEntity);

            }
            imgRepo.saveAll(imgs);
        }
    }

    //DELETE BẢNG TRUNG GIAN
    private boolean deleteProductSpecial(long productId) {
        try {
            this.productSpecialityRepo.deleteAllByProductId(productId);
            return true;
        } catch (Exception e) {
            log.error("xóa đặc trưng sản phẩm thất bại");
            return false;
        }
    }

    @Override
    public ServiceResponse getOneProductById(Long id) {
        try {
            ProductEntity result = productRepo.findById(id).orElse(null);

            result.setCheckChildren(this.checkExistChild(result.getId()));

            Map<Long, List<Object>> mapSpecial = this.checkSpecial(specialityRepo.findAll(), productSpecialityRepo.getByProductId(result.getId()));

            result.setMapSpecial(mapSpecial);

            result.setMapImg(mapImg(imgRepo.findAllByProductId(result.getId())));

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("Lỗi tìm chi tiết sản phẩm");
        }
    }

    @Override
    public ServiceResponse updateStatus(Long id) {
        try {
            ProductEntity product = productRepo.findById(id).orElse(null);
            if (product == null) return ServiceResponse.RESPONSE_ERROR("Error update status");

            product.setActive(product.getActive() == 1 ? -1 : 1);

            int active = productRepo.save(product).getActive();

            List<ProductEntity> productChildren = productRepo.findAllByParentId(id);

            productChildren.forEach(e -> {
                e.setActive(active);
            });
            productRepo.saveAll(productChildren);

            return ServiceResponse.RESPONSE_SUCCESS("Update status success");
        } catch (Exception e) {
            return ServiceResponse.RESPONSE_ERROR("Error update status");
        }
    }

    @Override
    public ServiceResponse filterProduct(Map<String, List<String>> dto) {

        String getLeft = "Select p.*";
        String getRight = "Select p2.*";
        StringBuffer QUERY_FILTER = new
                StringBuffer(" from  product p left join product_featured ps on p.id = ps.product_id ");

        StringBuffer QUERY_FILTER_CHILD = new
                StringBuffer(" from  product p left join product_featured ps on p.id = ps.product_id " +
                "join product p2 on p2.id = p.parent_id");
        QUERY_FILTER = createQueryFilter(QUERY_FILTER, dto);
        QUERY_FILTER_CHILD = createQueryFilter(QUERY_FILTER_CHILD, dto);


        String getParent = "( " + getLeft + QUERY_FILTER + " and p.parent_id is null )";
        String getParentByChild = "( " + getRight + QUERY_FILTER_CHILD + " and p2.parent_id is null  )";

        StringBuffer QUERY_RESULT = new StringBuffer(getParent + "UNION" + getParentByChild);
        System.out.println(QUERY_RESULT);

        /*order*/
        String sort = dto.get("sort").get(0);
        if (sort.equals("1")) {
            QUERY_RESULT.append(" ORDER BY created_date DESC ");
        } else if (sort.equals("2")) {
            QUERY_RESULT.append(" ORDER BY sold DESC ");
        } else if (sort.equals("3")) {
            QUERY_RESULT.append(" ORDER BY price_sell DESC ");
        } else {
            QUERY_RESULT.append(" ORDER BY price_sell ASC ");
        }

        int limit = Integer.valueOf(dto.get("limit").get(0));
        int page = (Integer.parseInt(dto.get("page").get(0)) - 1) * limit;
        QUERY_RESULT.append("  LIMIT " + page + "," + limit);

        Query query = entityManager.createNativeQuery(QUERY_RESULT.toString(), ProductEntity.class);

        List<ProductEntity> result = query.getResultList();
        favoriteService.setFavorite(result);
        return ServiceResponse.RESPONSE_SUCCESS(result);
    }

    @Override
    public ServiceResponse filterTotalPage(Map<String, List<String>> dto) {
        String getLeft = "Select p.*";
        String getRight = "Select p2.*";
        StringBuffer QUERY_FILTER = new
                StringBuffer(" from  product p left join product_featured ps on p.id = ps.product_id ");

        StringBuffer QUERY_FILTER_CHILD = new
                StringBuffer(" from  product p left join product_featured ps on p.id = ps.product_id " +
                "join product p2 on p2.id = p.parent_id ");
        QUERY_FILTER = createQueryFilter(QUERY_FILTER, dto);
        QUERY_FILTER_CHILD = createQueryFilter(QUERY_FILTER_CHILD, dto);

        String getParent = "( " + getLeft + QUERY_FILTER + " and p.parent_id is null )";
        String getParentByChild = "( " + getRight + QUERY_FILTER_CHILD + " and p2.parent_id is null  )";

        StringBuffer QUERY_RESULT = new StringBuffer(getParent + "UNION" + getParentByChild);

        /*order*/
        String sort = dto.get("sort").get(0);
        if (sort.equals("-1")) {
            QUERY_RESULT.append(" ORDER BY price_sell ASC ");
        } else if (sort.equals("1")) {
            QUERY_RESULT.append(" ORDER BY price_sell DESC ");
        } else {
            QUERY_RESULT.append(" ORDER BY created_date DESC ");
        }

        Query query = entityManager.createNativeQuery(QUERY_RESULT.toString(), ProductEntity.class);

        List<ProductEntity> entities = query.getResultList();

        int limit = Integer.valueOf(dto.get("limit").get(0));
        if (entities == null) return ServiceResponse.RESPONSE_SUCCESS(1);

        int result = (int) Math.ceil(((float) entities.size()) / limit);
        return ServiceResponse.RESPONSE_SUCCESS(result);
    }

    @Override
    public ServiceResponse searchProductByName(ProductRequestDto dto) {
        try {
            int page = dto.getPage() > 0 ? dto.getPage() - 1 : 0;
            dto.setPage(page * dto.getLimit());
            // bỏ dấu
            dto.setSearch(CodeUtil.removeAccent(dto.getSearch().trim()));
            // thêm %2 đầu%
            String nameSearch = StringUtils.isBlank(dto.getSearch())
                    ? "%%" : "%" + dto.getSearch().replaceAll(" ", "") + "%";

            List<ProductEntity> result = productRepo.searchByProductName(nameSearch, ON, dto.getPage(), dto.getLimit());
            favoriteService.setFavorite(result);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi tìm sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tìm kiếm");
        }
    }

    @Override
    public ServiceResponse totalProductBySearch(ProductRequestDto dto) {
        try {
            int page = dto.getPage() > 0 ? dto.getPage() - 1 : 0;
            dto.setPage(page * dto.getLimit());
            // bỏ dấu
            dto.setSearch(CodeUtil.removeAccent(dto.getSearch().trim()));
            // thêm %2 đầu%
            String nameSearch = StringUtils.isBlank(dto.getSearch())
                    ? "%%" : "%" + dto.getSearch().replaceAll(" ", "") + "%";

            int result = productRepo.totalProductBySearch(nameSearch, ON);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi tìm sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tìm kiếm");
        }
    }

    @Override
    public ServiceResponse totalPageProductBySearch(ProductRequestDto dto) {
        try {
            int page = dto.getPage() > 0 ? dto.getPage() - 1 : 0;
            dto.setPage(page * dto.getLimit());
            // bỏ dấu
            dto.setSearch(CodeUtil.removeAccent(dto.getSearch().trim()));
            // thêm %2 đầu%
            String nameSearch = StringUtils.isBlank(dto.getSearch())
                    ? "%%" : "%" + dto.getSearch().replaceAll(" ", "") + "%";

            int total = productRepo.totalProductBySearch(nameSearch, ON);
            int result = (int) Math.ceil(((float) total) / dto.getLimit());
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("lỗi tìm sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình tìm kiếm");
        }
    }


    // product detail
    @Override
    public ServiceResponse getProductDetailById(Long id) {
        try {
            ProductEntity productCurrent = productRepo.findByIdAndActive(id, ON).orElse(null);
            if (productCurrent == null) throw new NullPointerException();

            // convert
            ProductDetailResponseDto result = covertDto(productCurrent);
            result.setFavorite(favoriteService.getFavoriteProductDetail(result.getId()));

            // list Img
            List<String> listImg = imgRepo.findAllUrl(productCurrent.getId());
            result.setListImg(listImg);

            //
            //color of product
            // product child
            List<ProductEntity> productEntityList = new ArrayList<>();
            productEntityList.add(productCurrent);
            // product child
            productEntityList.addAll(productRepo.findAllByParentIdAndActive(productCurrent.getId(), ON));
            // color and size

            Integer totalPay = productEntityList.stream().reduce(0, (total, e) -> total + e.getSold(), Integer::sum);
            result.setTotalPay(totalPay);

            findColorAndSizeProduct(productEntityList, result);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get product detail");
            return ServiceResponse.RESPONSE_ERROR("Lỗi");
        }
    }

    // click color and size
    @Override
    public ServiceResponse getProductDetailBySpe(ProductDetailRequestDto dto) {
        try {

            // sử lý chọn size và màu color
            if (dto.getColorId() != null && dto.getSizeId() != null) {

                ProductEntity productParent = productRepo.findByIdAndActive(dto.getProductId(), ON).orElse(null);
                if (productParent == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                // sản phẩm con
                List<ProductEntity> productChildren = new ArrayList<>();
                productChildren.add(productParent);
                productChildren.addAll(productRepo.findAllByParentIdAndActive(productParent.getId(), ON));

                // Product choose
                ProductEntity productEntity = null;
                // check tim product
                for (ProductEntity pe : productChildren) {
                    List<Long> speId = productSpecialityRepo.getByProductId(pe.getId());
                    if (speId.contains(dto.getSizeId()) && speId.contains(dto.getColorId())) {
                        productEntity = pe;
                        break;
                    }
                }
                ProductDetailSpeResponseDto dto1 = null;
                if (productEntity != null) {
                    dto1 = covertDtoSpe(productEntity);
                    // list Img
                    List<String> listImg = imgRepo.findAllUrl(productEntity.getId());
                    dto1.setListImg(listImg);

                }
                return ServiceResponse.RESPONSE_SUCCESS(Arrays.asList(dto1));
            } else {
                List<ProductDetailSpeResponseDto> result = new ArrayList<>();

                SpecialityEntity spe;
                // sản phẩm cha
                ProductEntity productParent = productRepo.findById(dto.getProductId()).orElse(null);
                if (productParent == null) return ServiceResponse.RESPONSE_ERROR("Sản phẩm không tồn tại");
                // sản phẩm con
                List<ProductEntity> productChildren = new ArrayList<>();
                productChildren.add(productParent);
                productChildren.addAll(productRepo.findAllByParentIdAndActive(productParent.getId(), ON));

                for (ProductEntity pe : productChildren) {
                    boolean check;
                    // tim ra nhung san pham co size va mau nhu vay
                    if (dto.getSizeId() != null) {
                        check = productSpecialityRepo.existsByProductIdAndFeaturedId(pe.getId(), dto.getSizeId());
                    } else {
                        check = productSpecialityRepo.existsByProductIdAndFeaturedId(pe.getId(), dto.getColorId());
                    }

                    // neu dung thi tim dac truung cua san pham do
                    if (check) {

                        ProductDetailSpeResponseDto dto1 = covertDtoSpe(pe);
                        // list Img
                        List<String> listImg = imgRepo.findAllUrl(pe.getId());
                        dto1.setListImg(listImg);

                        List<Long> speId = productSpecialityRepo.getByProductId(pe.getId());
                        List<SpecialityEntity> speList = specialityRepo.findAllById(speId);

                        for (SpecialityEntity se : speList) {
                            if (dto.getSizeId() != null) {
                                if (COLOR_TYPE.equalsIgnoreCase(se.getFeaturedKey())) dto1.setColor(se);
                            } else {
                                if (SIZE_TYPE.equalsIgnoreCase(se.getFeaturedKey())) dto1.setSize(se);
                            }


                        }
                        result.add(dto1);
                    }
                }
                return ServiceResponse.RESPONSE_SUCCESS(result);
            }

        } catch (
                Exception e) {
            e.printStackTrace();
            log.error("error get product detail by spe");
            return null;
        }
    }

    // product detail


    // check color and size
    private void findColorAndSizeProduct(List<ProductEntity> productEntityList, ProductDetailResponseDto result) {

        // tìm màu và size của tất cả sản phẩm
        Set<SpecialityEntity> color = new TreeSet<>(Comparator.comparingLong(SpecialityEntity::getId));
//        Set<SpecialityEntity> color = new HashSet<>();
        Set<SpecialityEntity> size = new HashSet<>();
        boolean parentColorFound = false;

        int sizeList = productEntityList.size();
        for (int i = 0; i < sizeList; i++) {
            ProductEntity e = productEntityList.get(i);
            List<Long> speId = productSpecialityRepo.getByProductId(e.getId());
            if (speId.size() > 0) {
                List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);
                for (SpecialityEntity spe : speciality) {
                    if (i == 0) {
                        spe.setCheckParent(true);
                        if (COLOR_TYPE.equalsIgnoreCase(spe.getFeaturedKey())) {
                            parentColorFound = true;
                        }
                    }
                    if (COLOR_TYPE.equalsIgnoreCase(spe.getFeaturedKey())) color.add(spe);
                    if (SIZE_TYPE.equalsIgnoreCase(spe.getFeaturedKey())) size.add(spe);
                }
            }
        }

//        for (ProductEntity productEntity : productEntityList) {
//            // Kiểm tra xem đối tượng có phải là sản phẩm con không
//            if (productEntity.getParentId() != null) {
//                // Đây là sản phẩm con, xử lý size và color
//                List<Long> speId = productSpecialityRepo.getByProductId(productEntity.getId());
//
//                if (speId.size() > 0) {
//                    // Lấy danh sách đặc tính từ danh sách id
//                    List<SpecialityEntity> speciality = specialityRepo.findAllById(speId);
//
//                    for (SpecialityEntity spe : speciality) {
//                        if (COLOR_TYPE.equalsIgnoreCase(spe.getFeaturedKey())) {
//                            color.add(spe);
//                        }
//                        if (SIZE_TYPE.equalsIgnoreCase(spe.getFeaturedKey())) {
//                            size.add(spe);
//                        }
//                    }
//                }
//            }
//        }

        List<SpecialityEntity> colorConvert = new ArrayList<>();

        List<SpecialityEntity> sizeConvert = new ArrayList<>(size);

        if (!parentColorFound) {
//            List<SpecialityEntity> sortedColorList = color.stream()
//                    .sorted(Comparator.comparingLong(SpecialityEntity::getId))
//                    .collect(Collectors.toList());
//
//            SpecialityEntity firstNonParentColor = sortedColorList.stream()
//                    .filter(e -> !e.isCheckParent())
//                    .findFirst()
//                    .orElse(null);

//            System.out.println("color 1: " + firstNonParentColor);
            color.forEach(e -> {
//                System.out.println("color 2: " + e);
//                if (e == firstNonParentColor) {
//                    colorConvert.add(0, e);
//                } else {
                    colorConvert.add(e);
//                }
            });
        } else {
            color.forEach(e -> {
                if (e.isCheckParent()) {
                    colorConvert.add(0, e);
                } else {
                    colorConvert.add(e);
                }
            });
        }

        Collections.sort(sizeConvert);


        result.setColor(colorConvert);
        result.setSize(sizeConvert);
    }

    //convert dto
    //convert
    private ProductDetailResponseDto covertDto(ProductEntity productCurrent) {
        ProductDetailResponseDto result = ProductDetailResponseDto.builder()
                .id(productCurrent.getId())
//                .brandName(brandRepo.findById(productCurrent.getBrandId()).get().getBrandName())
                .brandName(productCurrent.getBrandId() != null ? brandRepo.findById(productCurrent.getBrandId()).get().getBrandName() : null)
                .productName(productCurrent.getProductName())
                .productCode(productCurrent.getProductCode())
                .priceSell(productCurrent.getPriceSell())
                .description(productCurrent.getDescription())
                .content(productCurrent.getContent())
                .categoryId(productCurrent.getCategoryId())
                .brandId(productCurrent.getBrandId())
                .parentId(productCurrent.getParentId())
                .img(productCurrent.getImg())
                .quantity(productCurrent.getQuantity())
                .sold(productCurrent.getSold())
                .sale(productCurrent.getSale())
                .rate(productCurrent.getRate())
                .totalRate(productCurrent.getTotalRate())
                .build();
        return result;
    }

    //convert by spe
    private ProductDetailSpeResponseDto covertDtoSpe(ProductEntity productCurrent) {
        ProductDetailSpeResponseDto result = ProductDetailSpeResponseDto.builder()
                .id(productCurrent.getId())
                .productName(productCurrent.getProductName())
                .productCode(productCurrent.getProductCode())
                .priceSell(productCurrent.getPriceSell())
                .description(productCurrent.getDescription())
                .content(productCurrent.getContent())
                .categoryId(productCurrent.getCategoryId())
                .brandId(productCurrent.getBrandId())
                .parentId(productCurrent.getParentId())
                .img(productCurrent.getImg())
                .quantity(productCurrent.getQuantity())
                .sold(productCurrent.getSold())
                .sale(productCurrent.getSale())
                .build();
        return result;
    }

    // tạo query
    private StringBuffer createQueryFilter(StringBuffer buffer, Map<String, List<String>> dto) {
        Set<String> keys = specialityRepo.getKey();

        for (String key : keys) {
            StringBuffer join = new StringBuffer(" join ");
            if (dto.get(key) == null || dto.get(key).isEmpty()) {
                continue;
            } else if ("color".equals(key) || "size".equals(key)) {
                // Điều kiện cho các feature của child (color và size)
                String param = dto.get(key).stream()
                        .map(i -> String.valueOf(i))
                        .collect(Collectors.joining(",", "(", ")"));
                buffer.append(join.append("(" + QUERY + " where ps.featured_id in " + param + ") " + key + " on " + key + ".id = p.id"));
            } else {
                String param = dto.get(key).stream().
                        map(i -> String.valueOf(i)).
                        collect(Collectors.joining(",", "(", ")"));
                buffer.append(join.append("(" + QUERY + " where ps.featured_id in " + param + ") " + key + " on " + key + ".id = p.id"));
            }
        }
        buffer.append(" where 1 = 1 ");
        if (dto.get("category").size() == 1 && !dto.get("category").isEmpty()) {
            String queryCate = "";
            CategoryEntity categoryEntity = categoryRepo.findById(Long.parseLong(dto.get("category").get(0))).orElse(null);
            if (categoryEntity.getParentId() != null) {
                queryCate += " and p.category_id = " + dto.get("category").get(0);
            } else {
                List<Long> cateIdChildren = categoryRepo.getAllIdByParentId(categoryEntity.getId());
                String param = cateIdChildren.stream().
                        map(i -> String.valueOf(i)).
                        collect(Collectors.joining(",", "(", ")"));
                queryCate += " and p.category_id in " + param;
            }
            buffer.append(queryCate);
        }

        if (dto.get("brand").size() == 1 && !dto.get("brand").isEmpty()) {
            String queryBrand = "";
            BrandEntity brandEntity = brandRepo.findById(Long.parseLong(dto.get("brand").get(0))).orElse(null);
            if (brandEntity != null) {
                queryBrand += " and p.brand_id = " + dto.get("brand").get(0);
            }
            buffer.append(queryBrand);
        }
        buffer.append(" and p.active = 1 ");


        return buffer;
    }

//    //    kiểm tra nếu có chọn đặc trung thì true không false
//    private Map<Long, Boolean> checkSpecial(List<SpecialityEntity> specialityEntities,
//                                            List<Long> selectId) {
//
//        Map<Long, Boolean> results = new HashMap<>();
//        for (int i = 0; i < specialityEntities.size(); i++) {
//            boolean check = false;
//            for (int j = 0; j < selectId.size(); j++) {
//                if (specialityEntities.get(i).getId().equals(selectId.get(j))) {
//                    results.put(specialityEntities.get(i).getId(), true);
//                    check = true;
//                    break;
//                }
//            }
//            if (check == false) {
//                results.put(specialityEntities.get(i).getId(), false);
//            }
//        }
//        return results;
//
//    }
    private Map<Long, List<Object>> checkSpecial(List<SpecialityEntity> specialityEntities, List<Long> selectId) {
        Map<Long, List<Object>> results = new HashMap<>();

        for (SpecialityEntity speciality : specialityEntities) {
            boolean isSelected = selectId.contains(speciality.getId());
            List<Object> value = Arrays.asList(isSelected, speciality.getFeaturedKey());
            results.put(speciality.getId(), value);
        }

        return results;
    }

    private Map<Long, String> mapImg(List<ImgEntity> imgEntities) {
        Map<Long, String> results = new HashMap<>();
        imgEntities.forEach(e -> {
            results.put(e.getId(), e.getImg());
        });
        return results;
    }
}
