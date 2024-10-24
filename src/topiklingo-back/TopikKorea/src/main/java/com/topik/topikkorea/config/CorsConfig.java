package com.topik.topikkorea.config;

import com.topik.topikkorea.config.interceptor.ThreadLocalCleanupInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    //    @Value(value = "${cors.allow.origins}")
//    private String[] allowedOrigins;
//
//    @Value(value = "${cors.allow.methods}")
//    private String[] allowedMethods;
    @Autowired
    private ThreadLocalCleanupInterceptor threadLocalCleanupInterceptor;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
//                .allowedOrigins(allowedOrigins)
//                .allowedMethods(allowedMethods)
//                .allowedHeaders("Origin", "Content-Type", "Accept", "Authorization")
                .allowedOriginPatterns("*")  // 모든 출처 허용
                .allowedMethods("*")  // 모든 HTTP 메서드 허용
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(threadLocalCleanupInterceptor);
    }
}
