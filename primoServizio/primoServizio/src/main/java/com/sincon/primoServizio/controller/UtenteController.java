package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;

@RestController
public class UtenteController {
    private final UtenteService utenteService;

    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    // Crea | modifica utente
    @PostMapping("/utente/crea")
    public ResponseEntity<String> registraUtente(@RequestBody Utente utente) throws SQLException {
        try {
            Utente utenteDaAssegnare = utenteService.getUtenteById(utente.getId());
            if(utenteDaAssegnare != null) {
                utenteService.modificaUtente(utenteDaAssegnare);

                return ResponseEntity.ok().body("Utente modificato con successo.");
            } else {
                utenteService.registraUtente(utente);

                return ResponseEntity.ok().body("Utente registrato con successo.");
            }
        } catch (SQLException | NotFoundException ex) {
            throw new SQLException();
        }
    }

    // Cancellazione logica utente
    @GetMapping("/utente/disattiva/{id}")
    public ResponseEntity<String> cancellazioneLogicaUtente(@RequestParam Long id) throws SQLException {
        try {
            Utente utenteDaCancellare = utenteService.getUtenteById(id);

            if(utenteDaCancellare != null &&  utenteService.cancellazioneLogicaUtente(id)) {

                return ResponseEntity.ok().body("Utente disattivato con successo.");
            } else {
                throw new NotFoundException();
            }
        } catch (SQLException ex) {
            throw new SQLException();
        }
    }

    // Eliminiazione utente
    @GetMapping("/utente/elimina/{id}")
    public ResponseEntity<String> eliminazioneUtente(@RequestParam Long id) throws SQLException {
        try {
            Utente utenteDaEliminare = utenteService.getUtenteById(id);
            if(utenteDaEliminare != null) {
                utenteService.eliminazioneUtente(id);

                return ResponseEntity.ok().body("Utente eliminato con successo.");
            } else {

                throw new NotFoundException();
            }
        } catch (SQLException ex) {
            throw new SQLException();
        }
    }

}
