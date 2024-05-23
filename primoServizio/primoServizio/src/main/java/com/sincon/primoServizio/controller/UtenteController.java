package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.mapperEntityDto.UtenteMapper;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.service.UtenteService;
import org.hibernate.annotations.processing.SQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
public class UtenteController {
    private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);
    private final UtenteService utenteService;
    private final UtenteMapper utenteMapper;
    @Autowired
    public UtenteController(UtenteService utenteService, UtenteMapper utenteMapper) {
        this.utenteService = utenteService;
        this.utenteMapper = utenteMapper;
    }

    @GetMapping("utente/lista")
    public List<UtenteDto> getListaUtenti() {
        return utenteService.getListaUtenti();
    }

    // Crea | modifica utente
//    @PostMapping("/utente/crea")
//    public ResponseEntity<String> registraUtente(@RequestBody UtenteDto utenteDto) throws SQLException {
//        try {
//            Utente utente = utenteMapper.utenteDtoToUtente(utenteDto);
//            Utente utenteDaAssegnare = utenteService.getUtenteById(utente.getId());
//
//            if(utenteDaAssegnare != null) {
//                utenteService.modificaUtente(utenteDaAssegnare);
//
//                return ResponseEntity.ok().body("Utente modificato con successo.");
//            } else {
//                utenteService.registraUtente(utente);
//
//                return ResponseEntity.ok().body("Utente registrato con successo.");
//            }
//        } catch (SQLException e) {
//            logger.error("Errore SQL in fase di registrazione/modifica utente", e);
//            throw new SQLException();
//
//        } catch (NotFoundException ex) {
//            throw new NotFoundException();
//        }
//    }

//    @PostMapping("/utente/crea")
//    public ResponseEntity<String> registraUtente(Utente utente) throws SQLException {
//        try {
//            utenteService.registraUtente(utente);
//        } catch (SQLException e) {
//            throw new SQLException();
//        }
//    }

    // Cancellazione logica utente
    @GetMapping("/utente/disattiva/{id}")
    public ResponseEntity<String> cancellazioneLogicaUtente(@PathVariable Long id) throws SQLException {
        try {
            Utente utenteDaCancellare = utenteService.getUtenteById(id);

            if(utenteDaCancellare != null &&  utenteService.cancellazioneLogicaUtente(id)) {

                return ResponseEntity.ok().body("Utente disattivato con successo.");
            } else {
                throw new NotFoundException();
            }
        } catch (SQLException e) {
            logger.error("Errore SQL in fase di cancellazione logica utente", e);
            throw new SQLException();
        }
    }

    // Eliminiazione utente
    @GetMapping("/utente/elimina/{id}")
    public ResponseEntity<String> eliminazioneUtente(@PathVariable Long id) throws SQLException {
        try {
            Utente utenteDaEliminare = utenteService.getUtenteById(id);
            if(utenteDaEliminare != null) {
                utenteService.eliminazioneUtente(id);

                return ResponseEntity.ok().body("Utente eliminato con successo.");
            } else {
                throw new NotFoundException();
            }

        } catch (SQLException e) {
            logger.error("Errore SQL in fase di eliminazione utente", e);
            throw new SQLException();
        }
    }
}
