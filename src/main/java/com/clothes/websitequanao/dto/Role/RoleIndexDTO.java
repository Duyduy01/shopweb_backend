package com.clothes.websitequanao.dto.Role;


import lombok.Getter;
import lombok.Setter;


import java.util.Objects;


@Getter
@Setter
public class RoleIndexDTO implements Comparable<RoleIndexDTO> {


    private String role;
    private Integer index;


    public RoleIndexDTO(String role, Integer index) {
        this.role = role;
        this.index = index;
    }


    public RoleIndexDTO(String role) {
        this.role = role;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RoleIndexDTO that = (RoleIndexDTO) o;
        return Objects.equals(role, that.role);
    }


    @Override
    public int hashCode() {
        return Objects.hash(role);
    }


    @Override
    public int compareTo(RoleIndexDTO o) {
        if (this.getIndex() == null || o.getIndex() == null)
            return 0;
        return this.index.compareTo(o.getIndex());
    }
}