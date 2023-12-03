package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ProductSpeciality;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductSpecialityRepo extends JpaRepository<ProductSpeciality, Long> {

    @Query(value = "select ps.featuredId from ProductSpeciality ps where ps.productId = :id ")
    List<Long> getByProductId(long id);

    @Modifying
    @Transactional
    void deleteAllByProductId(long id);

    List<ProductSpeciality> findByProductId(Long productId);
    // check exist
//    boolean existsByProductIdAndSpecialityId(long productId, long speId);

    boolean existsByProductIdAndFeaturedId(long productId, long speId);
}
