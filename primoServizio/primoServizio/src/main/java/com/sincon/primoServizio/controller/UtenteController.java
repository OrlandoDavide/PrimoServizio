package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.service.UtenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utente")
public class UtenteController {
    private static final Logger logger = LoggerFactory.getLogger(UtenteController.class);
    private final UtenteService utenteService;


    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService = utenteService;
    }

    @GetMapping("/lista")
    public ResponseEntity<?> getListaUtenti() {
        try {
            List<UtenteDto> listaUtenti = utenteService.getListaUtenti();

            if(listaUtenti.isEmpty()) {
                logger.error("NotFound-Exception : Lista utenti vuota.");
                throw new NotFoundException();
            }
            else return ResponseEntity.ok(listaUtenti);

        }  catch (Exception ex) {
            logger.error("Errore generico per lista utenti", ex);
            return ResponseEntity.status(500).body("Errore imprevisto durante l'operazione.");
        }
    }

    // Crea | modifica utente
    @PostMapping("/crea")
    public ResponseEntity<String> registraUtente(@RequestBody UtenteDto utenteDto) {
        try {
            UtenteDto utenteDaModificare = utenteService.getUtenteById(utenteDto.getId());

            if(utenteDaModificare != null) {
                utenteService.modificaUtente(utenteDaModificare);
                return ResponseEntity.ok().body("Utente modificato con successo.");

            } else {
                utenteService.registraUtente(utenteDto);
                return ResponseEntity.ok().body("Utente registrato con successo.");
            }

        }  catch (Exception ex) {
            logger.error("Errore in fase di modifica/registrazione utente", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore imprevisto durante l'operazione.");
        }
    }

    // Cancellazione logica utente
    @GetMapping("/disattiva/{id}")
    public ResponseEntity<String> cambioStatoUtente(@PathVariable Long id) {
        try {
            UtenteDto utente = utenteService.getUtenteById(id);

            if(utente != null) {
                utenteService.cambioStatoUtente(id);

                if(utente.isAttivo()) {
                    return ResponseEntity.ok().body("Utente disattivato con successo.");
                }
                else return ResponseEntity.ok().body("Utente attivato con successo.");
            }
            else {
                logger.error("Risorsa non trovata durante cambio stato utente. Id: " + id);
                throw new NotFoundException();
            }

        } catch (Exception ex) {
            logger.error("Errore durante il cambio stato utente con id: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Errore imprevisto durante il cambio stato dell'utente.");
        }
    }

    // Eliminiazione utente
    @GetMapping("/elimina/{id}")
    public ResponseEntity<String> eliminazioneUtente(@PathVariable Long id) {
        try {
            if(utenteService.getUtenteById(id) == null) {
                logger.error("Errore durante eliminazione utente. Id: " + id);
                throw new NotFoundException();
            }
            else {
                utenteService.eliminazioneUtente(id);

                return ResponseEntity.ok().body("Utente eliminato con successo.");
            }
        } catch (Exception ex) {
            logger.error("Errore in fase di eliminazione utente. ID: " + id, ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Errore imprevisto durante l'eliminazione dell'utente.");
        }
    }
}
