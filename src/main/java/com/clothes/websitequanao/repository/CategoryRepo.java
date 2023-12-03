package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<CategoryEntity, Long> {

    List<CategoryEntity> findAllByParentId(Long parentId);

    List<CategoryEntity> findAllByParentIdAndStatus(Long parentId, Integer status);

    CategoryEntity findByCategoryName(String cateName);

    @Query(value = "select c.id from CategoryEntity c where c.parentId = :parentId")
    List<Long> getAllIdByParentId(long parentId);
}
