package com.clothes.websitequanao.service;

import com.clothes.websitequanao.entity.RoleEntity;
import com.clothes.websitequanao.entity.UserRole;

public interface RoleService {

    RoleEntity findIdByCode (String roleName);

    void addUserRole (UserRole userRole);
}
