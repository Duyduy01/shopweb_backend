package com.clothes.websitequanao.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.clothes.websitequanao.common.Consts.address.*;
import static java.lang.Math.min;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.Collections.singletonList;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
public class CustomAuthorizationFilter extends OncePerRequestFilter {

    private static List<String> REQUEST_NO_AUTHEN = new ArrayList<>();

    static {
        REQUEST_NO_AUTHEN = (asList(
                "/api/v1/user/**", "/api/v2/user/**", "/connect/**", urlDistrict, urlWard, urlGetCity));


    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String url = request.getServletPath();
        //TODO TESST
        if (matchAny(url, REQUEST_NO_AUTHEN) && request.getHeader(AUTHORIZATION) == null) {
            filterChain.doFilter(request, response);
        } else {
            String authorizationHeader = request.getHeader(AUTHORIZATION);

            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                try {
                    String token = authorizationHeader.substring("Bearer ".length());
                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes(StandardCharsets.UTF_8));
                    JWTVerifier verifier = JWT.require(algorithm).build();// người xác dinh
                    DecodedJWT decodedJWT = verifier.verify(token);// xác minh token
                    String username = decodedJWT.getSubject();
                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    stream(roles).forEach(role -> {
                        authorities.add(new SimpleGrantedAuthority(role));
                    });
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    filterChain.doFilter(request, response);
                } catch (Exception e) {
                    log.info("Error logging in: {}", e.getMessage());
                    response.setHeader("error", e.getMessage());
                    response.setStatus(UNAUTHORIZED.value());
//                    response.sendError(FORBIDDEN.value());
                    Map<String, String> error = new HashMap<>();
                    // lỗi trả ra
                    error.put("error_message", e.getMessage());
                    response.setContentType(APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), error);
                }

            } else if (authorizationHeader == null) {
                response.setStatus(UNAUTHORIZED.value());

                Map<String, String> error = new HashMap<>();
                // lỗi trả ra
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            } else {
                filterChain.doFilter(request, response);
            }
        }
    }

    public static boolean matchAny(String source, List<String> urls) {
        if (urls == null) return false;
        int sourcelen = source.length();
        boolean isValid = true;
        for (String url : urls) {
            int urllen = url.length();
            int minlen = min(urllen, sourcelen);
            for (int i = 0; i < minlen; i++) {
                if (i < sourcelen && i < urllen && source.charAt(i) == url.charAt(i)) { //nếu trùng thì bỏ qua
                    if (i == minlen - 1 && sourcelen == urllen) {
                        return true;
                    }
                } else { //nếu khác -> check dấu *
                    if (url.charAt(i) == '*') {
                        try {
                            if (url.charAt(i + 1) == '*') { //nếu ** -> allow tất cả đằng sau
                                return true;
                            } else {
                                //source -> lấy chuỗi mới tính từ dấu /
                                //target -> lấy chuỗi mới tính từ dấu /
                                String sourceFromSlash = source.substring(i);
                                sourceFromSlash = sourceFromSlash.substring(sourceFromSlash.indexOf("/"));
                                String urlFromSlash = url.substring(i);
                                urlFromSlash = urlFromSlash.substring(urlFromSlash.indexOf("/"));
                                if (matchAny(sourceFromSlash, singletonList(urlFromSlash))) return true;
                            }
                        } catch (Exception e) {
                            //Nếu lấy i+1 lỗi -> check số / còn lại của source
                            //nếu số / còn la = 0 -> true
                            if (!source.substring(i).contains("/") && !url.substring(i).contains("/")) {
                                return true;
                            }
                        }
                    } else { //Nếu != dấu * -> false
                        isValid = false;
                        break;
                    }
                }
            }
        }
        return isValid;
    }
}
