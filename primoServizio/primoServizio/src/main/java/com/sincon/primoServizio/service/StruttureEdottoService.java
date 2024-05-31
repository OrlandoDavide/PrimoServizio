package com.sincon.primoServizio.service;

import com.sincon.primoServizio.dto.*;
import com.sincon.primoServizio.exception.NotFoundException;
import com.sincon.primoServizio.mapperEntityDto.OrgStrutturaMapper;
import com.sincon.primoServizio.repository.ComuneRepository;
import com.sincon.primoServizio.repository.OrgStrutturaRepositoryImpl;
import com.sincon.primoServizio.repository.OrganigrammaStrutturaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class StruttureEdottoService {
    private static final Logger logger = LoggerFactory.getLogger(StruttureEdottoService.class);
    private final OrgStrutturaRepositoryImpl orgRepositoryImpl;
    private final OrganigrammaStrutturaRepository strutturaRepository;
    private final OrgStrutturaMapper strutturaMapper;
    private final ComuneRepository comuneRepository;
    private final DizionarioService dizionarioService;
    private final UtenteService utenteService;
    private final ComuneService comuneService;

    @Autowired
    public StruttureEdottoService(OrgStrutturaRepositoryImpl orgRepositoryImpl,
                                  OrganigrammaStrutturaRepository strutturaRepository,
                                  OrgStrutturaMapper strutturaMapper,
                                  ComuneRepository comuneRepository,
                                  DizionarioService dizionarioService,
                                  UtenteService utenteService,
                                  ComuneService comuneService)
    {
        this.orgRepositoryImpl = orgRepositoryImpl;
        this.strutturaRepository = strutturaRepository;
        this.strutturaMapper = strutturaMapper;
        this.comuneRepository = comuneRepository;
        this.dizionarioService = dizionarioService;
        this.utenteService = utenteService;
        this.comuneService = comuneService;
    }

    public boolean salvaStruttura(OrganigrammaStrutturaDto struttura) {
        if (struttura != null) {
            strutturaRepository.save(strutturaMapper.orgStrutturaDtoToEntity(struttura));

            return true;
        } else return false;
    }

    // Set dati GENERALI STRUTTURA
    public void setDatiGeneraliStruttura(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        String codTipologiaStruttura = null;
        int evento;
        String tag = "";

        logger.info("STO SETTANDO I DATI GENERALI");
        try {
            do {
                evento = streamReader.next();
                if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                if (evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiGeneraliStruttura")) {
                    break;
                }

                if(evento == XMLStreamReader.START_ELEMENT) {
                    switch (tag) {
                        case "denominazione" :
                            struttura.setDenominazione(streamReader.getElementText());
                            break;
                        case "codCAP" :
                            struttura.setCodCap(streamReader.getElementText());
                            break;
                        case "email" :
                            struttura.setEmailTitolare(streamReader.getElementText());
                            break;
                        case "codice" :
                            struttura.setCodiceEdotto(Integer.parseInt(streamReader.getElementText()));
                            break;
                        case "comune" :
                            ComuneDto comune = comuneRepository.findByCodiceIstat(streamReader.getElementText()) != null ? comuneService.getComuneByCodIstat(streamReader.getElementText()) : null;
                            struttura.setComune(comune);
                            break;
                        case "codTipologiaStruttura" :
                            codTipologiaStruttura = streamReader.getElementText();
                            break;
                        case "dataIstituzione" :
                            LocalDate dataAttivazione = parseStringToLocalDate(streamReader.getElementText());

                            struttura.setDataAttivazione(dataAttivazione);
                            struttura.setDataApertura(dataAttivazione);
                            break;
                        case "dataChiusura" :
                            LocalDate dataChiusura = parseStringToLocalDate(streamReader.getElementText());

                            struttura.setDataChiusura(dataChiusura);
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiGeneraliStruttura")));

            //setAudit(struttura);
            DizionarioDto dizionario = dizionarioService.getDizionarioByCodifica(codTipologiaStruttura,"TIPOLOGIA_EDOTTO");
            if(dizionario != null) {
                struttura.setTipologiaEdotto(dizionario);
            }
        } finally {}
    }

    // Set ASL
    @Transactional
     public void setAsl(OrganigrammaStrutturaDto asl, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";
        logger.info("STO SETTANDO L'ASL");
        String codifica = null;

        try {
            do {
                evento = streamReader.next();
                if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                if(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                    break;
                }

                if(evento == XMLStreamReader.START_ELEMENT) {
                    switch (tag) {
                        case "codNazionale":
                            asl.setCodiceNSIS(streamReader.getElementText());
                            break;
                        case "email" :
                            asl.setEmailTitolare(streamReader.getElementText());
                            break;
                        case "telefono" :
                            asl.setTelefonoTitolare(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            asl.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            codifica = streamReader.getElementText();
                            break;
                    }
                }
            } while (evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria"));
            //asl.setAsl(true);

            if(codifica != null && !(codifica.isBlank())) {
                DizionarioDto dizionario = dizionarioService.getDizionarioByCodifica(codifica,"TIPO_GIURIDICA");

                if(dizionario != null) {
                    asl.setTipologiaEdotto(dizionario);
                }
            }
        } finally {}
    }

    // Set AUDIT
    public void setAudit(OrganigrammaStrutturaDto struttura) {
        UtenteDto utenteInSessione = utenteService.getUtenteInSessione();
        if(utenteInSessione != null) {
            if(struttura.getId() == null || struttura.getId() < 1) {
                struttura.setCreatedDate(LocalDate.now());
                struttura.setCreatedBy(utenteInSessione.getId());
                //todo struttura.setCreatedWith();
            } else {
                struttura.setModifiedDate(LocalDate.now());
                struttura.setModifiedBy(utenteInSessione.getId());
                //todo struttura.setModifiedWith();
            }
        } else {
            logger.error("Errore durante il recupero dell'utente in sessione.");
            throw new NotFoundException(404, "Nessun utente in sessione trovato.");
        }
    }

    // Set DISTRETTO SOCIO-SANITARIO
    public void setDistretto(OrganigrammaStrutturaDto distretto, XMLStreamReader streamReader) throws XMLStreamException {
        String codifica = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();
                if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                if(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                    break;
                }

                if(evento == XMLStreamReader.START_ELEMENT) {
                    switch (tag) {
                        case "partitaIVA" :
                            distretto.setCodice(streamReader.getElementText().trim());
                            break;
                        case "progDistretto" :
                            distretto.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()
                            ));
                            break;
                        case "codTipologiaGiuridica" :
                            codifica = streamReader.getElementText();
                            break;
                        case "email" :
                            distretto.setEmailTitolare(streamReader.getElementText().trim());
                            break;
                        case "telefono" :
                            distretto.setTelefonoTitolare(streamReader.getElementText().trim());
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            OrganigrammaStrutturaDto asl = findAslByCodice(streamReader.getElementText().trim()) != null ? findAslByCodice(streamReader.getElementText().trim()) : null;
                            distretto.setAsl(asl);
                            distretto.setParent(distretto.getAsl());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

            if(codifica != null && !(codifica.isBlank())) {
                distretto.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(codifica, "TIPO_GIURIDICA"));
            }
        } finally {}
    }

    public void setStruttura(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) {

    }

    // Set TIPOLOGIA ISTITUTO DI RICOVERO
    public void setTipoIstitutoDiRicovero(OrganigrammaStrutturaDto struttura, String tipoIstituto) {
        switch (tipoIstituto) {
            case "1":
                // Presidio ospedaliero
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24023L));
                break;
            case "2":
                // IRCCS pubblico
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24028L));
                break;
            case "3":
                // Azienda ospedaliera
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24029L));
                break;
            case "4":
                // Casa si cura privata accreditata
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24024L));
                break;
            case "5":
                // Casa di cura privata NON accreditata
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24025L));
                break;
            case "6":
                // Ente ecclesiastico
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24026L));
                break;
            case "7":
                // IRCCS privato
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24027L));
                break;
        }
    }

    // Set dati ISTITUTO DI RICOVERO
    public void setDatiIstitutoDiRicovero(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                if(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                    break;
                }

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "tipoIstituto" :
                            String tipoIstituto = streamReader.getElementText();
                            this.setTipoIstitutoDiRicovero(struttura, tipoIstituto);
                            break;
                        case "codNazionaleIstituto" :
                            struttura.setCodiceNSIS(streamReader.getElementText());
                            break;
                        case "codiceHSP11" :
                            struttura.setCodiceHsp11(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(),"TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            struttura.setParent(struttura.getAsl());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(struttura.getTipologiaEdotto() != null) {
                if(struttura.getTipologiaEdotto().getId() != 2 && struttura.getTipologiaEdotto().getId() != 3) {
                    struttura.setParent(struttura.getAsl());
                }
            }
        } finally {}
    }

    // Set dati FARMACIA
    public void setDatiFarmacia(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codProvinciale" :
                            struttura.setCodProvinciale(streamReader.getElementText());
                            break;
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }
        } finally {}
    }

    // Set dati AMBULATORIO SPECIALISTICO
    public void setDatiAmbulatorioSpecialstico(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
       Integer progDistretto = null;
       int evento;
       String tag = "";

       try {
           do {
               evento = streamReader.next();

               if(evento == XMLStreamReader.START_ELEMENT) {
                   tag = streamReader.getLocalName();

                   switch (tag) {
                       case "codiceSTS11" :
                           struttura.setCodiceNSIS(streamReader.getElementText());
                           break;
                       case "progDistretto" :
                           progDistretto = Integer.parseInt(streamReader.getElementText());
                           struttura.setProgDistretto(progDistretto);
                           break;
                       case "partitaIVA" :
                           struttura.setCodice(streamReader.getElementText());
                           break;
                       case "codTipologiaGiuridica" :
                           struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                           break;
                       case "codNazionaleASLAppartenenzaTerritoriale" :
                           struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                           break;
                   }
               }
           } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
           struttura.setParent(struttura.getDistretto());

           if(progDistretto != null) {
               struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
           }
       } finally {}
    }

    // Set dati STRUTTURA HOSPICE
    public void setDatiStrutturaHospice(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getAsl());
        } finally {}
    }

    // Set dati ISTITUTO PENITENZIARIO
    public void setDatiIstitutoPenitenziario(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    switch (tag) {
                        case "codiceMinisteriale" :
                            struttura.setCodMinisteriale(streamReader.getElementText());
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati OSPEDALE DI COMUNITA'
    public void setDatiOspedaleDiComunita(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    switch (tag) {
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                        case "codiceNSIS" :
                            struttura.setCodiceNSIS(streamReader.getElementText());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati POSTAZIONE EMERGENZA SANITARIA
    public void setDatiPostazioneEmergenzaSanitaria(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgDistretto(progDistretto);
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getDistretto());
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }
        } finally {}
    }

    // Set dati STRUTTURA SPECIALISTICA
    public void setDatiStrutturaSpecialistica(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    switch (tag) {
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getAsl());

        } finally {}
    }

    // Set dati PUNTO DI CONTINUITA' ASSISTENZIALE
    public void setDatiPuntoDiContinuitaAssistenziale(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgDistretto(progDistretto);
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getDistretto());
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }

        } finally {}
    }

    // Set dati REPARTO OSPEDALIERO
    public void setDatiRepartoOspedaliero(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer codStrutturaIstitutoDiRicovero = null;
        Integer progStabilimento = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codiceHSP12" :
                            struttura.setCodiceHsp12(streamReader.getElementText().trim());
                            break;
                        case "progStabilimento" :
                            progStabilimento = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgStabilimento(progStabilimento);
                            break;
                        case "progReparto" :
                            struttura.setProgReparto(Integer.parseInt(streamReader.getElementText().trim()));
                            break;
                        case "codStrutturaIstitutoDiRicovero" :
                            codStrutturaIstitutoDiRicovero = Integer.parseInt(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStrutturaIstitutoDiRicovero != null && progStabilimento != null) {
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStrutturaIstitutoDiRicovero, progStabilimento));
            }

        } finally {}
    }

    // Set dati SEDE MEDICINA DEI SERVIZI
    public void setDatiSedeMedicinaDeiServizi(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgDistretto(progDistretto);
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }
            struttura.setParent(struttura.getDistretto());

        } finally {}
    }

    // Set dati SERVIZIO TERRITORIALE DIPENDENZA PATOLOGICA
    public void setDatiServizioTerritorialeDipendenzaPatologica(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati SERVIZIO TERRITORIALE PREVENZIONE
    public void setDatiServizioTerritorialePrevenzione(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText().trim());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText().trim(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText().trim()));
                            break;
                        case "codiceNSIS" :
                            struttura.setCodiceNSIS(streamReader.getElementText().trim());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
        } finally {}
    }

    // Set dati SERVIZIO TERRITORIALE SALUTE MENTALE
    public void setDatiServizioTerritorialeSaluteMentale(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                        case "codiceSTS11" :
                            struttura.setCodiceNSIS(streamReader.getElementText().trim());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
        } finally {}
    }

    // Set dati SERVIZIO OSPEDALIERO
    public void setDatiServizioOspedaliero(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progStabilimento = null;
        Integer codStruttIstitutoDaRicovero = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codSpecialitaClinica" :
                            struttura.setCodiceSpecialitaClinica(streamReader.getElementText().trim());
                            break;
                        case "progServizio" :
                            struttura.setProgServizio(Integer.parseInt(streamReader.getElementText().trim()));
                            break;
                        case "progStabilimento" :
                            progStabilimento = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgStabilimento(progStabilimento);
                            break;
                        case "codStrutturaIstitutoDiRicovero" :
                            codStruttIstitutoDaRicovero = Integer.parseInt(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStruttIstitutoDaRicovero != null && progStabilimento != null) {
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStruttIstitutoDaRicovero, progStabilimento));
            }

        } finally {}
    }

    // Set dati RESIDENZA ASSISTENZIALE
    public void setDatiResidenzaAssistenziale(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText().trim());
                            break;
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgDistretto(progDistretto);
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getDistretto());
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }

        } finally {}
    }

    // Set dati STABILIMENTO OSPEDALIERO
    public void setDatiStabilimentoOspedaliero(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer codStruttura = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                if(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                    break;
                }

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "progStabilimento" :
                            struttura.setProgStabilimento(Integer.parseInt(streamReader.getElementText().trim()));
                            break;
                        case "codStrutturaIstitutoDiRicovero" :
                            codStruttura = Integer.parseInt(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStruttura != null) {
                struttura.setParent(findOneByCodiceEdotto(codStruttura));
            }

        } finally {}
    }

    // Set dati STRUTTURA RIABILITATIVA PSICHIATRICA
    public void setDatiStrutturaRiabilitativaPsichiatrica(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";
        Integer progDistretto = null;

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText().trim());
                            break;
                        case "progDistretto" :
                            progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                            struttura.setProgDistretto(progDistretto);
                            break;
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getDistretto());
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }
        } finally {}
    }

    // Set dati STRUTTURA CURE TERMALI
    public void setDatiStrutturaCureTermali(OrganigrammaStrutturaDto struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "partitaIVA" :
                            struttura.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            struttura.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(streamReader.getElementText(), "TIPO_GIURIDICA"));
                            break;
                        case "codNazionaleASLAppartenenzaTerritoriale" :
                            struttura.setAsl(findAslByCodice(streamReader.getElementText()));
                            break;
                        case "codiceSTS11" :
                            struttura.setCodiceSts11(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Cast da String a LocalDate, formato: "yyyy-MM-dd"
    public LocalDate parseStringToLocalDate(String stringData) {
        DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(stringData, formatoData);
    }

    // Verifica se l'indirizzo IP è locale/autorizzato
    public boolean isLocalAddress(String indirizzoIP) {
        //todo controlla se indirizzo è locale ??forse
        return false;
    }

    public OrganigrammaStrutturaDto findAslByCodice(String codNSIS) {
        return strutturaMapper.orgStrutturaEntityToDto(orgRepositoryImpl.findAslByCodiceNSIS(codNSIS));
    }

    public OrganigrammaStrutturaDto getStrutturaByCodiceEdottoIstitutoAndStabilimento(int codiceEdottoIstituto, int stabilimento) {
        return OrgStrutturaMapper.INSTANCE.orgStrutturaEntityToDto(orgRepositoryImpl.findOneByCodiceEdottoGrampaAndStabilimento(codiceEdottoIstituto, stabilimento));
    }

    public OrganigrammaStrutturaDto findOneByCodiceEdotto(int codiceEdotto) {
        return strutturaMapper.orgStrutturaEntityToDto(orgRepositoryImpl.findOneByCodiceEdotto(codiceEdotto));
    }

    public OrganigrammaStrutturaDto getDistrettoByProg(int prog, Long idAsl) {
        return strutturaMapper.orgStrutturaEntityToDto(orgRepositoryImpl.findDistretto(prog, idAsl));
    }

    public OrganigrammaStrutturaDto findOneByCodiceEdottoOfOriginal(int codiceEdotto) {
        return strutturaMapper.orgStrutturaEntityToDto(orgRepositoryImpl.findOneByCodiceEdottoOfOriginal(codiceEdotto));
    }

    public void identificaAndElaboraStruttura(OrganigrammaStrutturaDto struttura,XMLStreamReader streamReader, Optional<Path> xmlStruttura) throws XMLStreamException {
        if(xmlStruttura.isPresent()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(xmlStruttura.get().toFile()))) {
                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                XMLStreamReader filtroStream = inputFactory.createXMLStreamReader(reader);

                int evento;
                String tag = "";

                do {
                    evento = filtroStream.next();

                    if(evento != XMLStreamReader.CHARACTERS) tag = streamReader.getLocalName();

                    if(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                        break;
                    }

                    if(evento == XMLStreamReader.START_ELEMENT) {
                        tag = filtroStream.getLocalName();

                        switch (tag) {
                            case "datiFarmacia" :
                                setDatiFarmacia(struttura, streamReader);

                                if(struttura.getAsl() == null) {
                                    throw new IOException("ERRORE ASL");
                                }
                                break;
                            case "datiAmbulatorioSpecialstico" :
                                setDatiAmbulatorioSpecialstico(struttura, streamReader);
                                break;
                            case "datiStrutturaHospice" :
                                setDatiStrutturaHospice(struttura, streamReader);
                                break;
                            case "datiIstitutoDiRicovero" :
                                setDatiIstitutoDiRicovero(struttura, streamReader);
                                break;
                            case "datiIstitutoPenitenziario" :
                                setDatiIstitutoPenitenziario(struttura, streamReader);
                                break;
                            case "datiOspedaleDiComunita" :
                                setDatiOspedaleDiComunita(struttura, streamReader);
                                break;
                            case "datiPostazioneEmergenzaSanitaria" :
                                setDatiPostazioneEmergenzaSanitaria(struttura, streamReader);
                                break;
                            case "datiStrutturaSpecialistica" :
                                setDatiStrutturaSpecialistica(struttura, streamReader);
                                break;
                            case "datiPuntoDiContinuitaAssistenziale" :
                                setDatiPuntoDiContinuitaAssistenziale(struttura, streamReader);
                                break;
                            case "datiRepartoOspedaliero" :
                                setDatiRepartoOspedaliero(struttura, streamReader);
                                break;
                            case "datiSedeMedicinaDeiServizi" :
                                setDatiSedeMedicinaDeiServizi(struttura, streamReader);
                                break;
                            case "datiServizioTerritorialeDipendenzaPatologica" :
                                setDatiServizioTerritorialeDipendenzaPatologica(struttura, streamReader);
                                break;
                            case "datiServizioTerritorialePrevenzione" :
                                setDatiServizioTerritorialePrevenzione(struttura, streamReader);
                                break;
                            case "datiServizioTerritorialeSaluteMentale" :
                                setDatiServizioTerritorialeSaluteMentale(struttura, streamReader);
                                break;
                            case "datiServizioOspedaliero" :
                                setDatiServizioOspedaliero(struttura, streamReader);
                                break;
                            case "datiResidenzaAssistenziale" :
                                setDatiResidenzaAssistenziale(struttura, streamReader);
                                break;
                            case "datiStabilimentoOspedaliero" :
                                setDatiStabilimentoOspedaliero(struttura, streamReader);
                                break;
                            case "datiStrutturaRiabilitativaPsichiatrica" :
                                setDatiStrutturaRiabilitativaPsichiatrica(struttura, streamReader);
                                break;
                            case "daiStrutturaCureTermali" :
                                setDatiStrutturaCureTermali(struttura, streamReader);
                                break;
                        }
                    }
                } while(streamReader.hasNext());

            } catch (XMLStreamException | IOException e) {
                throw new XMLStreamException(e.getMessage());
            }
        } else throw new NotFoundException(404, "Risorsa non trovata");
    }

    // GET fileXML by TIPOLOGIA
    public Optional<Path> trovaXmlByTipologiaStruttura(Path cartellaEdotto, String tipologia) throws IOException {
        try(Stream<Path> stream = Files.list(cartellaEdotto)) {
            return stream
                    .filter(path -> {
                        logger.info("File trovato: " + path.getFileName());
                        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile()))) {
                            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                            while (streamReader.hasNext()) {
                                int evento = streamReader.next();

                                if(evento == XMLStreamReader.START_ELEMENT && streamReader.getLocalName().equalsIgnoreCase("codTipologiaStruttura")) {
                                    String tipologiaFile = streamReader.getElementText().trim();
                                    logger.info("Trovata codTipologiaStruttura: " + tipologiaFile);

                                    if(tipologiaFile.equalsIgnoreCase(tipologia)) {
                                        return true;
                                    }
                                } else if (evento == XMLStreamReader.END_ELEMENT && streamReader.getLocalName().equalsIgnoreCase("datiStrutturaSanitaria")) {
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            logger.error("Errore durante il recupero dell'XML per tipologia: " + e);
                        }
                        return false;
                    })
                    .findFirst();
        } catch (IOException e) {
            throw new IOException();
        }
    }

    public String getFileName(Optional<Path> xml) {
        return xml.map(path -> path.getFileName().toString()).orElse(null);
    }

    private void logEvent(XMLStreamReader reader, int event) {
        switch (event) {
            case XMLStreamReader.START_ELEMENT:
                logger.info("START_ELEMENT: " + reader.getLocalName());
                break;
            case XMLStreamReader.END_ELEMENT:
                logger.info("END_ELEMENT: " + reader.getLocalName());
                break;
            case XMLStreamReader.CHARACTERS:
                logger.info("CHARACTERS: " + reader.getText().trim());
                break;
            default:
                logger.info("EVENT: " + event);
        }
    }

    public void racchiudiDatiSanitari(XMLStreamReader reader, XMLStreamWriter writer) throws XMLStreamException {
        int evento;
        String tag = "";

        do {
            evento = reader.next();

            switch (evento) {
                case XMLStreamReader.START_ELEMENT:
                    tag = reader.getLocalName().trim();
                    writer.writeStartElement(tag);
                    int attributeCount = reader.getAttributeCount();
                    if (attributeCount > 0) {
                        for (int i = 0; i < attributeCount; i++) {
                            logger.info("Attributo " + i + ": " + reader.getAttributeLocalName(i) + " = " + reader.getAttributeValue(i));
                            writer.writeAttribute(reader.getAttributeLocalName(i), reader.getAttributeValue(i));
                        }
                    }
                    break;
                case XMLStreamReader.CHARACTERS:
                    writer.writeCharacters(reader.getText().trim());
                    break;
                case XMLStreamReader.END_ELEMENT:
                    tag = reader.getLocalName().trim();
                    logger.info("end element: " + tag);
                    writer.writeEndElement();
                    break;
            }
        } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
    }

}
