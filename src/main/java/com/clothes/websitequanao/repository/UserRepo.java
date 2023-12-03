package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.UserEntity;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepo extends JpaRepository<UserEntity,Long> {

   Optional<UserEntity> findByUserName(String userName);

   Optional<UserEntity> findByUserNameAndType(String userName, String type);

   Optional<UserEntity> findByUserNameAndStatus(String userName, Integer status);

   Optional<UserEntity> findByUserNameAndStatusAndType(String userName, Integer status, String type);

   int countAllByUserType(String userType);
}
