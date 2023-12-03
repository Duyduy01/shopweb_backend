package com.clothes.websitequanao.dto.request;

import com.clothes.websitequanao.entity.UserRole;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class AuthUser implements UserDetails {

    private static final long serialVersionUID = -2654547478565382629L;
    private String username;
    private String password;
    private String fullName;
    private Long rfrId; //This is userId
    private Long memberId;
    private String phone;
    private List<UserRole> permissionUserRoles;
    private Collection<? extends GrantedAuthority> authorities;

    public AuthUser(String username,
                    String password,
                    Collection<? extends GrantedAuthority> authorities,
                    Long rfrId,
                    Long memberId,
                    String phone,
                    List<UserRole> permissionUserRoles,
                    String fullName) {
        this.username = username;
        this.password = password;
        this.rfrId = rfrId;
        this.memberId = memberId;
        this.phone = phone;
        this.permissionUserRoles = permissionUserRoles;
        this.authorities = authorities;
        this.fullName = fullName;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
