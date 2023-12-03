package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.ProductBillEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductBillRepo extends JpaRepository<ProductBillEntity, Long> {

    // usser
    Optional<ProductBillEntity> findByProductId(Long productId);

    @Query(value = "select pbe.productId from ProductBillEntity pbe where pbe.billId = :billId")
    List<Long> listProductId(Long billId);


    Optional<ProductBillEntity> findByProductIdAndBillId(Long productId, Long billId);

    List<ProductBillEntity> findByBillId(Long billId);

    @Query(value = "select pbe.totalPrice from ProductBillEntity pbe where pbe.billId = :billId")
    List<BigDecimal> getAllTotalPrice(Long billId);

    Integer countAllByBillId(Long id);

    // user


    //admin
    List<ProductBillEntity> findAllByBillId(Long billId);

    @Query(value = "select sum(quantity) from product_bill where bill_id = :billId group by bill_id", nativeQuery = true)
    Integer sumQuantity(Long billId);
    //admin
}
