package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.BrandProductRequestDto;
import com.clothes.websitequanao.dto.request.brand.BrandAdminRequestDto;
import com.clothes.websitequanao.entity.BrandEntity;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.BrandRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.BrandService;
import com.clothes.websitequanao.service.FavoriteService;
import com.clothes.websitequanao.service.FileService;
import com.clothes.websitequanao.utils.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {

    private final BrandRepo brandRepo;

    private final ProductRepo productRepo;

    private final FavoriteService favoriteService;

    private final EntityManager entityManager;

    private final FileService fileService;

    @Override
    public ServiceResponse findAll() {
        try {
            List<BrandEntity> brandEntityList = brandRepo.findAllByStatus(USER_ACTIVE);
            return ServiceResponse.RESPONSE_SUCCESS("Thành công", brandEntityList);
        } catch (Exception e) {
            log.error("error find brand");
            return ServiceResponse.RESPONSE_ERROR("Lỗi");
        }
    }

    @Override
    public ServiceResponse getAllBrand() {
        try {
            List<BrandEntity> brandEntityList = brandRepo.findAll();
            brandEntityList.forEach(e -> {
                e.setBoolActive(e.getStatus() == 1 ? true : false);
            });
            return ServiceResponse.RESPONSE_SUCCESS("Thành công", brandEntityList);
        } catch (Exception e) {
            log.error("error find brand");
            return ServiceResponse.RESPONSE_ERROR("Lỗi");
        }
    }

    @Override
    public ServiceResponse addOrEditBrand(BrandAdminRequestDto dto) {
        try {
            if (dto.getId() == null) {
                BrandEntity result = addBrand(dto);
                return ServiceResponse.RESPONSE_SUCCESS("Thêm thương hiệu thành công", result);
            } else {
                BrandEntity result = brandRepo.findById(dto.getId()).orElse(null);
                if (result == null) return ServiceResponse.RESPONSE_ERROR("Thương hiệu không tồn tại");

                result.setBrandName(dto.getBrandName());
                result.setStatus(dto.getStatus());
                result.setContent(dto.getContent());
                result.setBoolActive(dto.getStatus() == 1 ? true : false);

                if (dto.getIcon() != null) {

                    fileService.deleteFile(FileUtil.getImgName(result.getIcon()));
                    String icon = (String) fileService.upload(dto.getIcon());
                    // save link database
                    result.setIcon(icon);
                }
                brandRepo.save(result);

                updateStatusProduct(result.getId(), result.getStatus());
                return ServiceResponse.RESPONSE_SUCCESS("Sửa thương hiệu thành công", result);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error add or edit brand");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình thay đổi thương hiệu");
        }
    }

    @Override
    public ServiceResponse updateStatus(Long id) {
        try {
            BrandEntity brandEntity = brandRepo.findById(id).orElse(null);
            if (brandEntity == null) return ServiceResponse.RESPONSE_ERROR("Thương hiệu không tồn tại");
            brandEntity.setStatus(brandEntity.getStatus() == 1 ? -1 : 1);
            brandRepo.save(brandEntity);

            // update status of product
            updateStatusProduct(brandEntity.getId(), brandEntity.getStatus());
            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi trạng thái của thương hiệu thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error update stautus brand");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình thay đổi trạng thái thương hiệu");
        }
    }

    @Override
    public ServiceResponse getProductByBrand(BrandProductRequestDto dto) {
        try {
            StringBuffer QUERY_RESULT = new StringBuffer("select * from product where brand_id = :brand and parent_id is null ");

            if (dto.getCategory() != null) {
                QUERY_RESULT.append(" and category_id = :category ");
            }

            if (dto.getSort() == 1) {
                QUERY_RESULT.append(" ORDER BY created_date DESC ");
            } else if (dto.getSort() == 2) {
                QUERY_RESULT.append(" ORDER BY sold DESC ");
            } else if (dto.getSort() == 3) {
                QUERY_RESULT.append(" ORDER BY price_sell DESC ");
            } else {
                QUERY_RESULT.append(" ORDER BY price_sell ASC ");
            }

            int limit = dto.getLimit();
            int page = (dto.getPage() - 1) * limit;
            QUERY_RESULT.append("  LIMIT " + page + "," + limit);

            Query query = entityManager.createNativeQuery(QUERY_RESULT.toString(), ProductEntity.class);
            query.setParameter("brand", dto.getBrand());
            if (dto.getCategory() != null) {
                query.setParameter("category", dto.getCategory());
            }

            List<ProductEntity> result = query.getResultList();
            favoriteService.setFavorite(result);

            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get product by brand");
            return ServiceResponse.RESPONSE_ERROR("error get product by brand");
        }
    }

    @Override
    public ServiceResponse getTotalByBrand(BrandProductRequestDto dto) {
        try {
            StringBuffer QUERY_RESULT = new StringBuffer("select * from product where brand_id = :brand and parent_id is null ");

            if (dto.getCategory() != null) {
                QUERY_RESULT.append(" and category_id = :category ");
            }

            if (dto.getSort() == 1) {
                QUERY_RESULT.append(" ORDER BY created_date DESC ");
            } else if (dto.getSort() == 2) {
                QUERY_RESULT.append(" ORDER BY sold DESC ");
            } else if (dto.getSort() == 3) {
                QUERY_RESULT.append(" ORDER BY price_sell DESC ");
            } else {
                QUERY_RESULT.append(" ORDER BY price_sell ASC ");
            }
            Query query = entityManager.createNativeQuery(QUERY_RESULT.toString(), ProductEntity.class);
            query.setParameter("brand", dto.getBrand());
            if (dto.getCategory() != null) {
                query.setParameter("category", dto.getCategory());
            }
            List<ProductEntity> productEntities = query.getResultList();
            Integer result = productEntities.size() == 0 ? 1 : productEntities.size();
            result = (int) Math.ceil(((float) result) / dto.getLimit());
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error get product by brand");
            return ServiceResponse.RESPONSE_ERROR("error get product by brand");
        }
    }

    private void updateStatusProduct(long brandId, int status) {
        List<ProductEntity> productEntities = productRepo.findAllByBrandId(brandId);
        if (productEntities.size() > 0) {
            productEntities.forEach(e -> {
                e.setActive(status);
            });
            productRepo.saveAll(productEntities);
        }
    }

    private BrandEntity addBrand(BrandAdminRequestDto dto) {
        String img = (String) fileService.upload(dto.getIcon());
        BrandEntity brandEntity = BrandEntity.builder()
                .brandName(dto.getBrandName())
                .status(dto.getStatus())
                .content(dto.getContent())
                .boolActive(dto.getStatus() == 1 ? true : false)
                .icon(img)
                .build();
        brandRepo.save(brandEntity);
        return brandEntity;
    }


}