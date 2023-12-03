package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.category.CateAdminRequestDto;
import com.clothes.websitequanao.entity.CategoryEntity;
import com.clothes.websitequanao.entity.ProductEntity;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.CategoryRepo;
import com.clothes.websitequanao.repository.ProductRepo;
import com.clothes.websitequanao.service.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;

    private final ProductRepo productRepo;

    // ADMIN
    @Override
    public ServiceResponse findAllByChild() {
        Map<String, List<CategoryEntity>> result = new HashMap<>();
        // tìm ra category parent
        List<CategoryEntity> cateEntities = categoryRepo.findAllByParentId(null);
        cateEntities.forEach(e -> {
            List<CategoryEntity> categoryEntities = categoryRepo.findAllByParentId(e.getId());
            result.put(e.getCategoryName(), categoryEntities);
        });
        return ServiceResponse.RESPONSE_SUCCESS(result);
    }

    // add or edit
    @Override
    public ServiceResponse addOrEditCate(CateAdminRequestDto dto) {
        try {
            if (dto.getId() == null) {
                CategoryEntity categoryEntity = CategoryEntity.builder()
                        .categoryName(dto.getCategoryName())
                        .status(dto.getStatus())
                        .content(dto.getContent())
                        .parentId(dto.getParentId() == 0 ? null : dto.getParentId())
                        .build();
                categoryEntity = categoryRepo.save(categoryEntity);

                categoryEntity.setBoolActive(categoryEntity.getStatus() == 1 ? true : false);

                return ServiceResponse.RESPONSE_SUCCESS("Thêm loại sản phẩm thành công", categoryEntity);

            } else {
                CategoryEntity categoryEntity = categoryRepo.findById(dto.getId()).orElse(null);
                if (categoryEntity == null)
                    return ServiceResponse.RESPONSE_ERROR("Lỗi loại sản phẩm muốn sửa không tồn tại");
                categoryEntity.setCategoryName(dto.getCategoryName());
                categoryEntity.setStatus(dto.getStatus());
                categoryEntity.setContent(dto.getContent());
                categoryEntity.setBoolActive(dto.getStatus() == 1 ? true : false);
                categoryEntity.setParentId(dto.getParentId());
                categoryRepo.save(categoryEntity);

                // set status product by category
                setStatusProduct(categoryEntity);

                return ServiceResponse.RESPONSE_SUCCESS("Sửa loại sản phẩm thành công", categoryEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi trong quá trình thay đổi loại sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình thay đổi loại sản phẩm");
        }

    }

    @Override
    public ServiceResponse updateStatus(Long id) {
        try {
            CategoryEntity cate = categoryRepo.findById(id).orElse(null);
            if (cate == null)
                return ServiceResponse.RESPONSE_ERROR("Lỗi loại sản phẩm muốn sửa không tồn tại");
            cate.setStatus(cate.getStatus() == 1 ? -1 : 1);

            setStatusProduct(cate);

            int status = categoryRepo.save(cate).getStatus();

            cate.setBoolActive(status == 1 ? true : false);
            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi trang thái của loại sản phẩm thành công", cate);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi trong quá trình thay đổi loại sản phẩm");
            return ServiceResponse.RESPONSE_ERROR("Lỗi trong quá trình thay đổi loại sản phẩm");
        }
    }

    // set status product by cate
    private void setStatusProduct(CategoryEntity categoryEntity) {
        List<ProductEntity> productEntities;
        if (categoryEntity.getParentId() == null) {
            List<Long> cateIdChildren = categoryRepo.getAllIdByParentId(categoryEntity.getId());
            productEntities = productRepo.findAllByCategoryIdIn(cateIdChildren);


            List<CategoryEntity> cateChildren = categoryRepo.findAllByParentId(categoryEntity.getId());
            cateChildren.forEach(e -> {
                e.setStatus(categoryEntity.getStatus());
            });
            categoryRepo.saveAll(cateChildren);

        } else {
            productEntities = productRepo.findAllByCategoryId(categoryEntity.getId());
        }
        productEntities.forEach(e -> {
            e.setActive(categoryEntity.getStatus());
        });
        productRepo.saveAll(productEntities);
    }

    ///
    @Override
    public ServiceResponse getAllParentId() {
        try {
            List<CategoryEntity> result = categoryRepo.findAllByParentId(null);
            result.forEach(e -> {
                List<CategoryEntity> categoryEntities = categoryRepo.findAllByParentId(e.getId());
                categoryEntities.forEach(child -> {
                    child.setBoolActive(child.getStatus() == 1 ? true : false);
                });
                e.setListChild(categoryEntities);
                e.setBoolActive(e.getStatus() == 1 ? true : false);

            });
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("error get parent cate");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra loại sản phẩm cha");
        }
    }
    /////

    @Override
    public ServiceResponse findAllCate() {
        Map<String, List<CategoryEntity>> result = new HashMap<>();
        // tìm ra category parent
        List<CategoryEntity> cateEntities = categoryRepo.findAllByParentIdAndStatus(null, USER_ACTIVE);
        cateEntities.forEach(e -> {
            List<CategoryEntity> categoryEntities = categoryRepo.findAllByParentIdAndStatus(e.getId(), USER_ACTIVE);
            result.put(e.getId() + "," + e.getCategoryName(), categoryEntities);
        });
        return ServiceResponse.RESPONSE_SUCCESS(result);
    }


}
