package com.sincon.primoServizio.config;

import com.sincon.primoServizio.controller.LoginController;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.service.JWTService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import io.micrometer.common.lang.NonNullApi;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@NonNullApi
@Component
public class Intercettatore implements HandlerInterceptor {

    private final JWTService jwtService;
    private Utente utenteInSessione;
    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    public Intercettatore(JWTService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String token = request.getHeader("Authorization");
        if(token == null || token.isBlank()) {
            logger.error(token);
            logger.error("IL TOKEN E' NULLO !!!");
            return false;
        } else {
            if (jwtService.validaToken(token)) {
                //request.setAttribute("Authorization", "Bearer " + token);
                logger.error("IL TOKEN E' VALIDO!");
                logger.error(token);
                return true;
            } else {
                logger.error("IL TOKEN NON E' VALIDO!");
                logger.error(token);
                return false;
            }
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        String token = request.getHeader("Authorization");

        if(token != null && jwtService.validaToken(token)) {
            if(token.startsWith("Bearer ")) {
                token = token.substring(7).trim();
            }
            Claims claims = jwtService.getAllClaimsFromToken(token);
            String idUtente = claims.get("id", String.class);

            if(idUtente != null) {
                 request.getSession().setAttribute("id", idUtente);
            }
        }
    }

}
