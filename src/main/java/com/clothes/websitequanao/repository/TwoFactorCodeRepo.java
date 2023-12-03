package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.TwoFactorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface TwoFactorCodeRepo extends JpaRepository<TwoFactorCode, Long> {
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM two_factor_code WHERE user_id = ?1", nativeQuery = true)
    void deleteByUserId(Long userId);

    Optional<TwoFactorCode> findTop1ByUserIdOrderByCreatedAtDesc(long userId);

}
