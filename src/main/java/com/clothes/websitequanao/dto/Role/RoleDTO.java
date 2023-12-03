package com.clothes.websitequanao.dto.Role;

import lombok.*;

import java.util.List;

import static java.lang.Math.min;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {
    private String role;
    private List<String> allow;
    private List<String> deny;


}

