package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByRoleName(String roleName);

    @Query(value = "select re.id from RoleEntity  re where re.roleCode = :roleCode")
    Long getIdRole(String roleCode);
}
