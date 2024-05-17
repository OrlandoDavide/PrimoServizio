package com.sincon.primoServizio.service;

import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.SQLException;

@Service
public class UtenteService {

    private final UtenteRepository utenteRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository,
                         BCryptPasswordEncoder passwordEncoder) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Utente getUtenteById(Long id) {
        return utenteRepository.findUtenteById(id);
    }

    @Transactional
    public Utente getUtenteByEmail(String email) throws NotFoundException, SQLException {
        return utenteRepository.findUtenteByEmail(email);
    }

    @Transactional
    public void registraUtente(Utente utente) throws SQLException {
        Utente utenteRegistrato = utenteRepository.findUtenteById(utente.getId());

        if(utenteRegistrato != null) {
            utenteRegistrato.setEmail(utente.getEmail());
            utenteRegistrato.setAttivo(utente.isAttivo());

            String criptedPassword = passwordEncoder.encode(utente.getPassword());
            utenteRegistrato.setPassword(criptedPassword);

            utenteRepository.save(utenteRegistrato);
        } else {
            utenteRepository.save(utente);
        }
    }

    // Crea nuovo utente
    @Transactional
    public void creaUtente(Utente utente) {
        if(utente != null) {
            utenteRepository.save(utente);
        }
    }

    // Modifica utente
    @Transactional
    public void modificaUtente(Utente utente) throws NotFoundException {
        Utente utenteEsistente = utenteRepository.findUtenteById(utente.getId());
        if(utenteEsistente != null) {
            utenteEsistente.setEmail(utente.getEmail());
            utenteEsistente.setPassword(utente.getPassword());
            utenteEsistente.setAttivo(utente.isAttivo());

            utenteRepository.save(utenteEsistente);
        }
    }

    // Cancellazione logica utente
    @Transactional
    public boolean cancellazioneLogicaUtente(Long id) throws SQLException, NotFoundException {
        Utente utente = utenteRepository.findUtenteById(id);

        if(utente != null) {
            utente.setAttivo(false);

            utenteRepository.save(utente);

            return true;
        } else {
            return false;
        }
    }

    // Eliminazione utente
    public boolean eliminazioneUtente(Long id) throws SQLException, NotFoundException{
        Utente utenteDaEliminare = utenteRepository.findUtenteById(id);

        if(utenteDaEliminare != null) {
            utenteRepository.delete(utenteDaEliminare);

            return true;
        } else {
            return false;
        }
    }
}
