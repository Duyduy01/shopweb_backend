package com.clothes.websitequanao.facade.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.clothes.websitequanao.common.Consts;
import com.clothes.websitequanao.dto.request.AuthUser;
import com.clothes.websitequanao.dto.request.UserDTO;
import com.clothes.websitequanao.dto.request.VerifyCodeDTO;
import com.clothes.websitequanao.dto.response.LoginResponse;
import com.clothes.websitequanao.entity.RoleEntity;
import com.clothes.websitequanao.entity.TwoFactorCode;
import com.clothes.websitequanao.entity.UserEntity;
import com.clothes.websitequanao.entity.UserRole;
import com.clothes.websitequanao.exception.ErrorCodes;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.facade.UserFacade;
import com.clothes.websitequanao.service.RoleService;
import com.clothes.websitequanao.service.TwoFactorService;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.Timestamp;
import java.util.Date;
import java.util.stream.Collectors;

import static com.clothes.websitequanao.common.Consts.UserType.*;
import static com.clothes.websitequanao.exception.ServiceResponse.RESPONSE_ERROR;
import static com.clothes.websitequanao.exception.ServiceResponse.RESPONSE_SUCCESS;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserFacadeImpl implements UserFacade {

    private final UserService userService;

    private final RoleService roleService;

    private final TwoFactorService twoFactorService;

    private final AuthenticationManager authenticationManager;

    private Authentication authentication;

    @Override
    public ServiceResponse register(UserDTO userDTO) {
        long nowInMs = System.currentTimeMillis();
        Timestamp now = new Timestamp(nowInMs);

        UserEntity user = userService.findByUsernameAndStatus(userDTO.getEmail().trim(), USER_ACTIVE);

        if (user != null) {
            if (ACTIVE.equalsIgnoreCase(user.getType())) {
                ErrorCodes err = ErrorCodes.ACCOUNT_ALREADY_EXISTS;
                return RESPONSE_ERROR(err.message(), err);
            } else {
                user.setUserName(userDTO.getEmail());
                user.setPassword(userService.bcryptPass(userDTO.getPassword()));
                user.setType(PENDING);
                user.setEmail(userDTO.getEmail());
                user.setPhone(userDTO.getPhone());
                user.setAddress(userDTO.getAddress());
                user.setStatus(USER_ACTIVE);
                user.setFullName(userDTO.getFullName());
                user.setUserType(USER);
            }
        } else {
            user = UserEntity.builder()
                    .userName(userDTO.getEmail())
                    .password(userService.bcryptPass(userDTO.getPassword()))
                    .type(PENDING)
                    .email(userDTO.getEmail())
                    .phone(userDTO.getPhone())
                    .address(userDTO.getAddress())
                    .status(USER_ACTIVE)
                    .fullName(userDTO.getFullName())
                    .userType(USER).build();
        }

        userService.saveUser(user);

        TwoFactorCode factorCode = twoFactorService.generateVerifyCode(user, VerifyCodeDTO.builder().build());
        factorCode.setFullName(user.getFullName());
        factorCode.setReasonSendEmail(Consts.SendEmailReason.FOR_REGISTER);
        factorCode.setEmail(user.getEmail());
        twoFactorService.sendVerificationCode(factorCode);
        return RESPONSE_SUCCESS("Register success, an verification code has been sent to your email", null);

    }

    @Override
    public ServiceResponse sendCode(UserDTO userDTO) {
        try {
            UserEntity user = userService.findByUsernameAndStatusAndType(userDTO.getEmail().trim(), USER_ACTIVE, PENDING);
            TwoFactorCode factorCode = twoFactorService.generateVerifyCode(user, VerifyCodeDTO.builder().build());
            factorCode.setFullName(user.getFullName());
            factorCode.setReasonSendEmail(Consts.SendEmailReason.FOR_REGISTER);
            factorCode.setEmail(user.getEmail());
            twoFactorService.sendVerificationCode(factorCode);
            return RESPONSE_SUCCESS("Send code to email user success", null);
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("send code to email user fail");
        }
    }

    @Override
    public ServiceResponse verifyEmail(UserDTO userDTO) {
        try {

            long nowInMs = System.currentTimeMillis();
            Timestamp now = new Timestamp(nowInMs);
            System.out.println(userDTO.getEmail());
            UserEntity user = this.userService.findByUsernameAndStatus(userDTO.getEmail(), USER_ACTIVE);

            if (user != null && userDTO.getVerificationCode() != null) {
                String code = twoFactorService.getVerificationCode(user.getId());

                if (userDTO.getVerificationCode().equals(code)) {

                    user.setType(ACTIVE);
                    userService.saveUser(user);


                    RoleEntity role = roleService.findIdByCode(USER);


                    UserRole userRole = UserRole.builder()
                            .userId(user.getId())
                            .roleId(role.getId())
                            .roleCode(USER)
                            .build();

                    roleService.addUserRole(userRole);

                    // Xoá hết verity code nếu ngưởi dùng đã active thành công
                    twoFactorService.deleteVerifyCodeByUserId(user.getId());

                    return ServiceResponse.RESPONSE_SUCCESS("Email verification successful");
                }
            }
            return ServiceResponse.RESPONSE_ERROR("User does not exist and verification code is null");
        } catch (Exception e) {
            e.printStackTrace();
            return ServiceResponse.RESPONSE_ERROR("verify user fail");
        }
    }

    @Override
    public ServiceResponse login(UserDTO userDTO) {
        userDTO.setEmail(userDTO.getUsername());
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword()));

        AuthUser user = (AuthUser) authentication.getPrincipal();

        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
        String access_token = JWT.create()
                .withClaim("id", user.getRfrId())
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 10 * 100000))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
//        String refresh_token = JWT.create()
//                .withSubject(user.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
//                .withIssuer(request.getRequestURL().toString())
//                .sign(algorithm);
//        response.setHeader("access_token",access_token);
//        response.setHeader("refresh_token",refresh_token);
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("access_token", access_token);
//        tokens.put("refresh_token", refresh_token);
        user.setPassword(null);
        LoginResponse tokens = LoginResponse.builder()
                .accessToken(access_token)
                .refreshToken(null)
                .user(user)
                .build();
        return ServiceResponse.RESPONSE_SUCCESS("Login success", tokens);
    }


}
