package com.sincon.primoServizio.utils;

import com.sincon.primoServizio.config.ApplicationConfig.JwtConfig;
import com.sincon.primoServizio.service.JWTService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(classes = {JwtConfig.class, JWTService.class})
public class JwtTokenTests {

    private JWTService jwtService;

    @MockBean
    private JwtConfig jwtConfig;

    @BeforeEach
    public void setUp() {
        jwtService = new JWTService(jwtConfig);
    }

    @Test
    public void jwtService_generaToken_notNull() {
        String SECRET_KEY = "davide-orlandoProva987456321primoServizio741258963753159";

        doReturn(SECRET_KEY).when(jwtConfig).getSECRET_KEY();
        String token = jwtService.generaToken("test@example.it");

        System.out.println(token);
        assertNotNull(token);
    }

    @Test
    public void jwtService_validaToken_true() {
        String SECRET_KEY = "davide-orlandoProva987456321primoServizio741258963753159";

        doReturn(SECRET_KEY).when(jwtConfig).getSECRET_KEY();
        String token = jwtService.generaToken("test@example.it");


        assertTrue(jwtService.validaToken(token));
    }
}
