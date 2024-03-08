package com.clothes.websitequanao.repository;

import com.clothes.websitequanao.entity.UserEntity;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface UserRepo extends JpaRepository<UserEntity,Long> {

   Optional<UserEntity> findByUserName(String userName);

   Optional<UserEntity> findByUserNameAndType(String userName, String type);

   Optional<UserEntity> findByUserNameAndStatus(String userName, Integer status);

   Optional<UserEntity> findByUserNameAndStatusAndType(String userName, Integer status, String type);

   List<UserEntity> findAllByIdInAndStatus(List<Long> userIds, int status);

   int countAllByUserType(String userType);
}
