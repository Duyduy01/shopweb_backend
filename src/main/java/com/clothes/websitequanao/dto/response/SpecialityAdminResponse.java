package com.clothes.websitequanao.dto.response;

import com.clothes.websitequanao.entity.SpecialityEntity;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SpecialityAdminResponse {
    private String typeName;
    private List<SpecialityEntity> specialityEntities;
}
