package com.joje.palanoto.common.config;

import com.joje.palanoto.common.interceptor.CommonInterceptor;
import com.joje.palanoto.common.interceptor.JwtTokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebMvc
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    /**
     * 인터셉터 핸들러
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new CommonInterceptor()).addPathPatterns("/**");
//		registry.addInterceptor(new JwtTokenInterceptor()).addPathPatterns("/**").excludePathPatterns("/palanoto/account/login");
        WebMvcConfigurer.super.addInterceptors(registry);
    }
}
