package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.entity.RoleEntity;
import com.clothes.websitequanao.entity.UserRole;
import com.clothes.websitequanao.exception.ErrorCodes;
import com.clothes.websitequanao.exception.ServiceException;
import com.clothes.websitequanao.repository.RoleRepo;
import com.clothes.websitequanao.repository.UserRoleRepo;
import com.clothes.websitequanao.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepo roleRepo;

    private final UserRoleRepo userRoleRepo;

    @Override
    public RoleEntity findIdByCode(String roleName) {
        Optional<RoleEntity> role = roleRepo.findByRoleName(roleName);
        return role.isPresent() ? role.get() : null;
    }

    @Override
    public void addUserRole(UserRole userRole) {
        try {
            userRoleRepo.save(userRole);
        } catch (Exception e) {
            ErrorCodes err = ErrorCodes.BAD_REQUEST;
            log.error("Error add user role");
            e.printStackTrace();
            throw new ServiceException(err.status(), "Error add user role", err.statusCode());

        }
    }
}
