package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.service.JWTService;
import com.sincon.primoServizio.service.UtenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    private final UtenteService utenteService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JWTService jwtService;

    @Autowired
    public LoginController(UtenteService utenteService,
                           BCryptPasswordEncoder bCryptPasswordEncoder,
                           JWTService jwtService)
    {
        this.utenteService = utenteService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody String email, @RequestBody String password) {
        try {
            Utente utente = utenteService.getUtenteByEmail(email);

            if(utente != null && bCryptPasswordEncoder.matches(password, utente.getPassword())) {
               if(!(utente.isAttivo())) {
                   return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non autorizzato");
               }
               else {
                   String token = this.jwtService.generaToken(utente.getEmail());

                   return ResponseEntity.ok().body(token);
               }
            }
            else throw new NotFoundException();

        } catch (Exception ex) {
            logger.error("Errore in fase di login con email: " + email, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore in fase di login");
        }
    }
}
