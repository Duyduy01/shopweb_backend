package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.BillEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BillRepo extends JpaRepository<BillEntity, Long> {

    Optional<BillEntity> findByUserIdAndStatus(Long userId, int status);

    Optional<BillEntity> findByIdAndStatus(Long id, int status);
    Optional<BillEntity> findByIdAndStatusNot(Long id, int status);

    // status == 0
    List<BillEntity> findAllByUserIdAndStatusNotOrderByBillDateDesc(Long userId, Integer status,Pageable pageable);

    Integer countAllByUserId(Long userId);
    // status == 0

    // status != 0
    List<BillEntity> findAllByUserIdAndStatusOrderByBillDateDesc(Long userId,int status, Pageable pageable);

    Integer countAllByUserIdAndStatus(Long userId, int status);
    // status != 0

    //admin
    List<BillEntity> findAllByStatusNotOrderByBillDateDesc(Integer status);

    int countAllByStatus(int status);

    //admin
}
