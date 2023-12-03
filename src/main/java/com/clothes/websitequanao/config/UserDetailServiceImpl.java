package com.clothes.websitequanao.config;


import com.clothes.websitequanao.dto.request.AuthUser;
import com.clothes.websitequanao.entity.UserEntity;
import com.clothes.websitequanao.entity.UserRole;
import com.clothes.websitequanao.exception.ErrorCodes;
import com.clothes.websitequanao.exception.ServiceException;
import com.clothes.websitequanao.repository.UserRoleRepo;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.clothes.websitequanao.common.Consts.UserType.ACTIVE;
import static com.clothes.websitequanao.common.Consts.UserType.USER_ACTIVE;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;

    private final UserRoleRepo permissionUserRoleRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userService.findByUsernameAndStatusAndType(username, USER_ACTIVE, ACTIVE);
        if (user == null) {
            log.error("Invalid user {}", username);
            ErrorCodes err = ErrorCodes.UNAUTHORIZED;
            throw new ServiceException(err.status(), err.message(), err.statusCode());
        }
        List<UserRole> pers = permissionUserRoleRepository.findAllByUserId(user.getId());
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (UserRole per : pers) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + per.getRoleCode()));
        }
        AuthUser authUser = new AuthUser(username, user.getPassword(), authorities, user.getId(), null, user.getPhone(), pers, user.getFullName());
        return authUser;
    }
}
