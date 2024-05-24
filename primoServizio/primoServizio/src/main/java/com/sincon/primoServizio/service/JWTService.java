package com.sincon.primoServizio.service;

import com.sincon.primoServizio.config.ApplicationConfig.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTService {

    private static final Logger logger = LoggerFactory.getLogger(JWTService.class);
    private final JwtConfig jwtConfig;
    @Autowired
    public JWTService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
    }


    // Generazione SECRET_KEY con algoritmo di crittografia HMAC
    public SecretKey generaHmacSecretKey() {
        byte[] secretBytes = jwtConfig.getSECRET_KEY().getBytes();

        return Keys.hmacShaKeyFor(secretBytes);
    }

    // Generazione token
    @Transactional
    public String generaToken(String email) {
        Map<String, String> claims = new HashMap<>();
        claims.put("email", email);

        System.out.println(email);

        return Jwts.builder()
                .header()
                .add("typ", "jwt")
                .and()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(this.generaHmacSecretKey())
                .compact();
    }

    // Recupero claims dal token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .verifyWith(generaHmacSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // Validazione token
    public boolean validaToken(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);
            Date expirationTime = claims.getExpiration();

            System.out.println(expirationTime);
            return expirationTime != null && expirationTime.after(new Date());
        } catch (JwtException e) {
            return false;
        }
    }
}
