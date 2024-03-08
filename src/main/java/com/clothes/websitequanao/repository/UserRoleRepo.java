package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleRepo extends JpaRepository<UserRole, Long> {
    List<UserRole> findAllByUserId(long userId);

    UserRole findByUserId(long userId);

    @Query(value = "select ur.userId from UserRole ur where ur.roleId = :roleId")
    List<Long> getByRoleId(long roleId);
}
