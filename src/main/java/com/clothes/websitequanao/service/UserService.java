package com.clothes.websitequanao.service;

import com.clothes.websitequanao.dto.request.UserAdminRequestDTO;
import com.clothes.websitequanao.dto.request.manager.UseInfoRequestDto;
import com.clothes.websitequanao.entity.UserEntity;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface UserService {
    UserEntity findByUsernameAndStatus(String userName, Integer status);


    UserEntity findByUsernameAndStatusAndType(String userName, Integer status, String type);

    String bcryptPass(String clientPass);

    void saveUser(UserEntity user);

    UserEntity findByUsernameAndType(String userName, String type);


    //admin
    ServiceResponse getAllUserManu();
    // admin


    // supper admin
    ServiceResponse getCustomer(Integer status);

    ServiceResponse addCustomer(UserAdminRequestDTO dto);

    ServiceResponse updateStatus(Long id);

    //supper admin


    // user
    ServiceResponse getUserDetail();

    ServiceResponse updateUserInfo(UseInfoRequestDto dto);

}
