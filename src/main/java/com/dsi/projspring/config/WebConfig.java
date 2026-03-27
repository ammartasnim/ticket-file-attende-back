//package com.dsi.projspring.config;
//
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.servlet.config.annotation.CorsRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@Configuration
//public class WebConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/**") // Allow all endpoints
//                .allowedOrigins("http://localhost:8100", "http://localhost:4200") // Ionic and Angular defaults
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP verbs
//                .allowedHeaders("*") // Allow all headers (Authorization, Content-Type, etc.)
//                .allowCredentials(true); // Crucial for Basic Auth/Session handling
//    }
//}