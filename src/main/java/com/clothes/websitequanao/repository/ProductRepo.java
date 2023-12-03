package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductRepo extends JpaRepository<ProductEntity, Long> {
    List<ProductEntity> findByBrandId(Long brandId);


    List<ProductEntity> findAllByParentId(Long parentId);

    boolean existsByParentId(Long parentId);

//    boolean existsByParentIdAndSizeAndColor(Long parentId, String size, String color);

    // check user name
    boolean existsAllByProductName(String productName);

    // receipt
    List<ProductEntity> findAllByParentIdAndActive(Long parentId, int active);

    Optional<ProductEntity> findByIdAndActive(Long id, int active);

    // brand
    List<ProductEntity> findAllByBrandId(long id);


    //cate
    List<ProductEntity> findAllByCategoryId(long cateId);

    List<ProductEntity> findAllByCategoryIdIn(List<Long> cateIdChildren);


//    // product detail
//    List<ProductEntity> findAllByParentIdAndActive(Long parentId, int active);


    // home
    List<ProductEntity> findAllByParentIdAndActiveOrderByCreatedDateDesc(Long parentId, int active, Pageable pageable);

    //search
    @Query(value = "SELECT p.* FROM product p where p.search  Like :name and p.parent_id is null  and p.active = :status LIMIT :page ,:limit", nativeQuery = true)
    List<ProductEntity> searchByProductName(String name, int status, int page, int limit);

    // total
    @Query(value = "SELECT count(p.id) FROM product p where p.search  Like :name and p.parent_id is null  and p.active = :status ", nativeQuery = true)
    int totalProductBySearch(String name, int status);
    //search


    @Query(value = "select pe.id from ProductEntity pe where pe.parentId = :productId and pe.active = 1")
    List<Long> listIdByChild(Long productId);



}
