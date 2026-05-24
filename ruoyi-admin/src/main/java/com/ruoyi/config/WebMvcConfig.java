package com.ruoyi.config;

import com.ruoyi.intercept.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**")          // 拦截哪些接口
                .excludePathPatterns(
                        "/api/user/wxLogin",
                        "/api/oss/upload",
                        "/api/banners/**",
                        "/api/Aicherb",
                        "/api/Aicherb/**"
                );// 放行登录/回调接口
    }
}
