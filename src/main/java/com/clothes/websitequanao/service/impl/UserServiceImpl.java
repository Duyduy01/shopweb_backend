package com.clothes.websitequanao.service.impl;

import com.clothes.websitequanao.dto.request.UserAdminRequestDTO;
import com.clothes.websitequanao.dto.request.manager.UseInfoRequestDto;
import com.clothes.websitequanao.entity.*;
import com.clothes.websitequanao.exception.ErrorCodes;
import com.clothes.websitequanao.exception.ServiceException;
import com.clothes.websitequanao.exception.ServiceResponse;
import com.clothes.websitequanao.repository.RoleRepo;
import com.clothes.websitequanao.repository.UserRepo;
import com.clothes.websitequanao.repository.UserRoleRepo;
import com.clothes.websitequanao.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.clothes.websitequanao.common.Consts.UserType.*;
import static com.clothes.websitequanao.exception.ServiceResponse.RESPONSE_ERROR;
import static com.clothes.websitequanao.exception.ServiceResponse.RESPONSE_SUCCESS;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepo userRepo;

    private final UserRoleRepo userRoleRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;


    @Override
    public UserEntity findByUsernameAndStatus(String userName, Integer status) {
        Optional<UserEntity> user = userRepo.findByUserNameAndStatus(userName, status);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public UserEntity findByUsernameAndStatusAndType(String userName, Integer status, String type) {
        Optional<UserEntity> user = userRepo.findByUserNameAndStatusAndType(userName, status, type);
        return user.isPresent() ? user.get() : null;
    }

    @Override
    public String bcryptPass(String clientPass) {
        try {
//            String originPass = DecryptionUtils.getInstance().decryptWithPrivateKey(clientPass);
            return passwordEncoder.encode(clientPass);
        } catch (Exception e) {
            ErrorCodes err = ErrorCodes.BAD_REQUEST;
            log.error("Fail to encrypt user password");
            e.printStackTrace();
            throw new ServiceException(err.status(), "Incorrect password format", err.statusCode());

        }
    }

    @Override
    public void saveUser(UserEntity user) {
        try {
//            String originPass = DecryptionUtils.getInstance().decryptWithPrivateKey(clientPass);
            userRepo.save(user);
        } catch (Exception e) {
            ErrorCodes err = ErrorCodes.BAD_REQUEST;
            log.error("Fail to encrypt user password");
            e.printStackTrace();
            throw new ServiceException(err.status(), "Error save user", err.statusCode());

        }

    }

    @Override
    public UserEntity findByUsernameAndType(String userName, String type) {
        Optional<UserEntity> user = userRepo.findByUserNameAndType(userName, type);
        return user.isPresent() ? user.get() : null;
    }

    // admin
    @Override
    public ServiceResponse getAllUserManu() {
        try {

            List<Long> userId = userRoleRepo.getByRoleId(MANUFACTURE_ID);
            List<UserEntity> result = userRepo.findAllById(userId);
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error user manu");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra nhà cung cấp");
        }
    }

    @Override
    public ServiceResponse getCustomer(Integer status) {
        try {
            List<Long> userId = new ArrayList<>();
            if (status == 0) {
                userId = userRoleRepo.getByRoleId(USER_ID);
            } else if (status == 1) {
                userId = userRoleRepo.getByRoleId(ADMIN_ID);
            } else {
                userId = userRoleRepo.getByRoleId(MANUFACTURE_ID);
            }

            List<UserEntity> result = userRepo.findAllById(userId);
            result.forEach(e -> {
                e.setBoolActive(e.getStatus() == 1 ? true : false);
            });
            return ServiceResponse.RESPONSE_SUCCESS(result);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error user");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra người cùng");
        }
    }

    @Override
    public ServiceResponse addCustomer(UserAdminRequestDTO dto) {
        try {
            UserEntity user = userRepo.findByUserNameAndStatus(dto.getEmail().trim(), USER_ACTIVE).orElse(null);

            if (user != null) {
                ErrorCodes err = ErrorCodes.ACCOUNT_ALREADY_EXISTS;
                return RESPONSE_ERROR("Email đã được sử dụng", err);

            }

            LocalDate birthday = LocalDate.parse(dto.getBirthday());
            user = UserEntity.builder()
                    .userName(dto.getEmail())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .type(ACTIVE)
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .address(dto.getAddress())
                    .gender(dto.getGender())
                    .birthday(birthday)
                    .city(dto.getCity())
                    .district(dto.getDistrict())
                    .ward(dto.getWard())
                    .status(USER_INACTIVE)
                    .fullName(dto.getFullName())
                    .userType(dto.getType()).build();

            Long id = userRepo.save(user).getId();


            Long roleId = roleRepo.getIdRole(dto.getType());

            UserRole role = UserRole.builder()
                    .userId(id)
                    .roleId(roleId)
                    .roleCode(dto.getType())
                    .build();
            userRoleRepo.save(role);

            return RESPONSE_SUCCESS("Thêm tài khoản thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi thêm tài khoản ");
            return RESPONSE_ERROR("Lỗi thêm tài khoản ");
        }
    }

    @Override
    public ServiceResponse updateStatus(Long id) {
        try {
            UserEntity user = userRepo.findById(id).orElseThrow(() -> new NullPointerException("Tài khoản không tồn tại"));

            user.setStatus(user.getStatus() == 1 ? -1 : 1);

            userRepo.save(user);

            int newStatus = user.getStatus();

            return RESPONSE_SUCCESS("Thay đổi trạng thái thành công", newStatus);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi thay đổi trạng thái ");
            return RESPONSE_ERROR("Lỗi thay đổi trạng thái");
        }
    }


    @Override
    public ServiceResponse getUserDetail() {
        try {
            Long userId = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                userId = userRepo.findByUserName(currentUserName).get().getId();
            }
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            UserEntity user = userRepo.findById(userId).orElse(null);

            if (user == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            return ServiceResponse.RESPONSE_SUCCESS(user);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Lỗi lấy ra thông tin của người dùng");
            return ServiceResponse.RESPONSE_ERROR("Lỗi lấy ra thông tin của người dùng");
        }

    }

    @Override
    public ServiceResponse updateUserInfo(UseInfoRequestDto dto) {
        try {
            Long userId = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!(authentication instanceof AnonymousAuthenticationToken)) {
                String currentUserName = authentication.getName();
                userId = userRepo.findByUserName(currentUserName).get().getId();
            }
            if (userId == null) return ServiceResponse.RESPONSE_ERROR("Người dùng không tồn tại");

            LocalDate birthday = LocalDate.parse(dto.getBirthday());
            UserEntity user = userRepo.findById(userId).orElseThrow(() -> new NullPointerException("User does not exist"));
            user.setFullName(dto.getFullName());
            user.setPhone(dto.getPhone());
            user.setBirthday(birthday);
            user.setGender(dto.getGender());
            user.setCity(dto.getCity());
            user.setDistrict(dto.getDistrict());
            user.setWard(dto.getWard());
            user.setWard(dto.getWard());
            user.setAddress(dto.getAddress());
            userRepo.save(user);
            return ServiceResponse.RESPONSE_SUCCESS("Thay đổi thông tin thành công");
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Thay đổi thông thất bại");
            return ServiceResponse.RESPONSE_ERROR("Thay đổi thông tin thất bại");
        }
    }

}
