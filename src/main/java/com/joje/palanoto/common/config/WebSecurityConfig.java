package com.joje.palanoto.common.config;

import com.joje.palanoto.common.security.JwtAccessDeniedHandler;
import com.joje.palanoto.common.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) // 어노테이션을 이용한 접근 제한 허용
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfig jwtSecurityConfig;

    /**
     * 시큐리티 제외
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/assets/**", "/favicon.ico");
    }

    /**
     * 권한 계층 구조
     */
    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        RoleHierarchyImpl roleHierarchyImpl = new RoleHierarchyImpl();
        roleHierarchyImpl.setHierarchy("ROLE_ROOT > ROLE_ADMIN > ROLE_USER");
        return roleHierarchyImpl;
    }

    /**
     * 권한 계층 구조 등록
     */
    @Bean
    public AccessDecisionVoter<? extends Object> roleVoter(){
//        RoleHierarchyVoter roleHierarchyVoter = new RoleHierarchyVoter(roleHierarchy());
//        return roleHierarchyVoter;
        return new RoleHierarchyVoter(roleHierarchy());
    }

    /**
     * 시큐리티 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//          token을 사용하는 방식이기 때문에 csrf를 disable
                .csrf().disable()

//          Exception을 핸들링할 때 직접 만든 클래스를 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

//          세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .formLogin().disable()
                .headers().frameOptions().disable()

//         	권한 인증
                .and()
                .authorizeRequests() // HttpServletRequest를 사용하는 요청들에 대한 접근제한을 설정
                .antMatchers("/palanoto/user/**").hasRole("USER")
                .antMatchers("/palanoto/admin/**").hasRole("ADMIN")
                .antMatchers("/palanoto/root/**").hasRole("ROOT")
//          그 외 요청은 인증을 하지 않음.
                .anyRequest().permitAll()

//          JwtSecurityConfig 클래스 적용
                .and()
                .apply(jwtSecurityConfig);
    }

    /**
     * Cors 설정
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.setAllowedMethods(Arrays.asList("HEAD", "GET", "POST", "PUT", "DELETE"));
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * 패스워드 인코더
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
