package com.heyongqiang.zyz.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


@Configuration
public class WebMvcConfig implements WebMvcConfigurer {


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        //跨域配置
        registry.addMapping("/**")
                // 放行哪些原始域
                 .allowedOrigins("*")  // 2.2 之前的版本用的
//                .allowedOriginPatterns("*")
                // 是否发送 Cookie 信息
                .allowCredentials(true)
                // 放行哪些原始域（请求方式）
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 放行哪些头部信息
                .allowedHeaders("*")
                // 暴露哪些头部信息（因为跨域访问默认不能获取全部头部信息）
                .exposedHeaders("Header1", "Header2");
    }




}
