package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.SystemParam;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SystemParamRepo extends JpaRepository<SystemParam, Long> {
    @Query("SELECT s FROM SystemParam s WHERE s.key = ?1")
    SystemParam findByKey(String key);

    @Query("SELECT s FROM SystemParam s WHERE s.key in (?1)")
    List<SystemParam> findByKeys(List<String> keys);
}
