package com.clothes.websitequanao.config;

import com.clothes.websitequanao.filter.CustomAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.clothes.websitequanao.common.Consts.address.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserDetailServiceImpl userDetailService;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
//        CustomAnthenticationFilter customAnthenticationFilter = new CustomAnthenticationFilter(authenticationManagerBean());
//        customAnthenticationFilter.setFilterProcessesUrl("/api/v1/user/login");

        http.csrf().disable();
        http.httpBasic().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.authorizeRequests().antMatchers("/api/v3/user/**").hasAnyAuthority("ROLE_USER");
        http.authorizeRequests().antMatchers("/api/v1/admin/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/supper/admin/**").hasAnyAuthority("ROLE_SUPER_ADMIN");
        http.authorizeRequests().antMatchers("/api/v1/user/**", "/api/v2/user/**", urlGetCity, urlWard, urlDistrict).permitAll();

//        http
//                .authorizeRequests()
//                .antMatchers("/connect", "/connect/info", "/connect/**").permitAll() // Cho phép tất cả mọi người truy cập vào endpoint "/connect"
////                .anyRequest().authenticated() // Các endpoint khác yêu cầu xác thực
//                .and()
//                .csrf().disable();
//        http.cors().and().csrf().disable().authorizeRequests().antMatchers("/connect", "/connect/info", "/connect/**").permitAll();

        http.authorizeRequests().anyRequest().authenticated();

//        http.addFilter(customAnthenticationFilter);
        http.addFilterBefore(new CustomAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);


    }


//    @Bean
//    public WebMvcConfigurer webMvcConfigurerAdapter() {
//        return new WebMvcConfigurerAdapter() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/**")
//                        .allowedOrigins("http://localhost:8080")
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD");
//            }
//        };
//    }


}
