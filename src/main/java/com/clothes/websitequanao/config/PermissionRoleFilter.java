//package com.clothes.doanwebsitequanaodaiduong.config;
//
//
//import com.clothes.doanwebsitequanaodaiduong.dto.Role.RoleDTO;
//import com.clothes.doanwebsitequanaodaiduong.service.BaseService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//
//import static com.clothes.doanwebsitequanaodaiduong.common.Consts.UserType.ADMIN;
//import static com.clothes.doanwebsitequanaodaiduong.dto.Role.RoleDTO.matchAny;
//import static com.clothes.doanwebsitequanaodaiduong.exception.ErrorCodes.FORBIDDEN;
//import static java.util.Arrays.asList;
//import static org.hibernate.cfg.AvailableSettings.USER;
//
//@Component
//@RequiredArgsConstructor
//public class PermissionRoleFilter extends OncePerRequestFilter {
//
//    private final BaseService baseService;
//
//    private static final List<RoleDTO> ROLES_AUTHORIZED_REQUESTS = new ArrayList<>();
//
//    static {
//        ROLES_AUTHORIZED_REQUESTS.add(RoleDTO.builder()
//                .role(ADMIN)
//                .allow(asList("**"))
//                .deny(asList(
//                        "/api/v1/admin/**",
//                        "/api/v1/district/*/user/*"))
//                .build());
//
//        ROLES_AUTHORIZED_REQUESTS.add(RoleDTO.builder()
//                .role(USER)
//                .allow(asList("/api/v1/customers",
//                        "**"
//                ))
//                .deny(asList(
//                ))
//                .build());
//    }
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String url = request.getServletPath();
//        List<String> roles = baseService.getCurrentRoles();
//
//        int numOfDeny = 0;
//        for (String userRole : roles) {
//            for (RoleDTO roleDTO : ROLES_AUTHORIZED_REQUESTS) {
//                if (roleDTO.getRole().equals(userRole)) {
//                    if (matchAny(url, roleDTO.getDeny())) {
//                        numOfDeny = numOfDeny + 1;
//                    } else if (matchAny(url, roleDTO.getAllow())) {
//                        doFilter(request, response, filterChain);
//                        return;
//                    }
//                }
//            }
//        }
//        if (numOfDeny > 0) {
//            response.setStatus(FORBIDDEN.statusCode());
//            return;
//        }
//        doFilter(request, response, filterChain);
//    }
//}
