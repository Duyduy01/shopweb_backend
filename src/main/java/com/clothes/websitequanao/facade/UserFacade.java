package com.clothes.websitequanao.facade;

import com.clothes.websitequanao.dto.request.UserDTO;
import com.clothes.websitequanao.exception.ServiceResponse;

public interface UserFacade {
    ServiceResponse register(UserDTO userDTO);

    ServiceResponse sendCode(UserDTO userDTO);

    ServiceResponse verifyEmail(UserDTO userDTO);

    ServiceResponse login(UserDTO userDTO);
}
