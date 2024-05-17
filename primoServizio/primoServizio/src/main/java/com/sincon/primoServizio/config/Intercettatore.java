package com.sincon.primoServizio.config;

import com.sincon.primoServizio.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@NonNullApi
@Component
public class Intercettatore implements HandlerInterceptor {

    private final JWTService jwtService;

    @Autowired
    public Intercettatore(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if(request.getMethod().equals("OPTIONS")) {
            return true;
        } else {
            String token = request.getHeader("Authorization");

            if(token != null && jwtService.validaToken(token)) {
                request.setAttribute("Authorization", "Bearer" + token);

                return true;
            }
            else return false;
        }
    }

}
