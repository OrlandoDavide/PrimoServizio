package com.sincon.primoServizio.config;

import com.sincon.primoServizio.service.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class IntercettatoreConfig implements WebMvcConfigurer {

    private final JWTService jwtService;

    @Autowired
    public IntercettatoreConfig(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new Intercettatore(jwtService))
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/strutture/aggiorna-edotto",
                         "/public/**",
                         "/login"
                 );
    }

}
