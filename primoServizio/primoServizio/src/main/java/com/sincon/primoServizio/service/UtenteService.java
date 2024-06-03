package com.sincon.primoServizio.service;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.exception.DuplicateResourceException;
import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.mapperEntityDto.UtenteMapper;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.repository.UtenteRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(UtenteService.class);
    private final UtenteRepository utenteRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UtenteMapper utenteMapper;

    @Autowired
    public UtenteService(UtenteRepository utenteRepository,
                         BCryptPasswordEncoder passwordEncoder,
                         UtenteMapper utenteMapper) {
        this.utenteRepository = utenteRepository;
        this.passwordEncoder = passwordEncoder;
        this.utenteMapper = utenteMapper;
    }

    // Find utente by ID
    public UtenteDto getUtenteById(Long id) throws NotFoundException {
        try {
            Utente utente = utenteRepository.findUtenteById(id);

            return utenteMapper.utenteEntityToDto(utente);
        } finally {}
    }

    // Find utente by EMAIL
    public Utente getUtenteByEmail(String email) throws NotFoundException {
        try {
            return utenteRepository.findUtenteByEmail(email);
        } finally {}
    }

    // GET lista utenti
    @Transactional
    public List<UtenteDto> getListaUtenti() throws NotFoundException {
        try {
            List<Utente> listaUtenti = utenteRepository.findAll();
            return utenteMapper.listaEntityToDto(listaUtenti);
        } finally {}
    }

    // Registra utente
    @Transactional
    public void registraUtente(UtenteDto utenteDto) {
        try {
            Utente nuovoUtente = getUtenteByEmail(utenteDto.getEmail());
            if(nuovoUtente != null) {
                throw new DuplicateResourceException(400, String.format("Questa email è già presente nel database: %s", utenteDto.getEmail()));
            } else {
                nuovoUtente = utenteMapper.utenteDtoToEntity(utenteDto);
                nuovoUtente.setPassword(passwordEncoder.encode(utenteDto.getPassword()));

                utenteRepository.save(nuovoUtente);
            }
        } finally {}
    }

    // Modifica utente
    @Transactional
    public void modificaUtente(UtenteDto utenteDto) throws NotFoundException {
        try {
            Utente utenteDaModificare = utenteRepository.findUtenteById(utenteDto.getId());

            if (utenteDaModificare != null) {
                utenteDaModificare.setEmail(utenteDto.getEmail());
                utenteDaModificare.setPassword(utenteDto.getPassword());
                utenteDaModificare.setAttivo(utenteDto.isAttivo());

                utenteRepository.save(utenteDaModificare);
            } else {
                logger.error("Errore durante modifica utente.Risorsa non trovata. Id: " + utenteDto.getId());
                throw new NotFoundException(404, "Utente non trovato");
            };
        } finally {}
    }

    // Cancellazione logica utente
    @Transactional
    public void cambioStatoUtente(Long id) throws NotFoundException {
        Utente utente = utenteRepository.findUtenteById(id);
        boolean statoUtente = utente.isAttivo();
        utente.setAttivo(!statoUtente);

        utenteRepository.save(utente);
    }

    // Eliminazione utente
    public void eliminazioneUtente(Long id) throws NotFoundException {
        try {
            Utente utenteDaEliminare = utenteRepository.findUtenteById(id);
            utenteRepository.delete(utenteDaEliminare);
        } finally {}
    }

    // Recupera utente in sessione
//    public UtenteDto getUtenteInSessione() {
//        HttpSession sessione = request.getSession(false);
//
//        if(sessione != null) {
//            String idUtente = String.valueOf(request.getSession().getAttribute("id"));
//            if(idUtente != null) {
//                return getUtenteById(Long.valueOf(idUtente));
//            } else throw new NotFoundException();
//        } else throw new NotFoundException(404, "Sessione non trovata");
//    }
}
