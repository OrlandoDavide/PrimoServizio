package com.sincon.primoServizio.service;

import com.sincon.primoServizio.dto.UtenteDto;
import com.sincon.primoServizio.model.Comune;
import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.repository.ComuneRepository;
import com.sincon.primoServizio.repository.OrgStrutturaRepositoryImpl;
import com.sincon.primoServizio.repository.OrganigrammaStrutturaRepository;
import jakarta.servlet.http.HttpServletRequest;
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
    private final ComuneRepository comuneRepository;
    private final DizionarioService dizionarioService;
    private final UtenteService utenteService;

    @Autowired
    public StruttureEdottoService(OrgStrutturaRepositoryImpl orgRepositoryImpl,
                                  OrganigrammaStrutturaRepository strutturaRepository,
                                  ComuneRepository comuneRepository,
                                  DizionarioService dizionarioService,
                                  UtenteService utenteService)
    {
        this.orgRepositoryImpl = orgRepositoryImpl;
        this.strutturaRepository = strutturaRepository;
        this.comuneRepository = comuneRepository;
        this.dizionarioService = dizionarioService;
        this.utenteService = utenteService;
    }

    public boolean salvaStruttura(OrganigrammaStruttura struttura) {
        if (struttura != null) {
            strutturaRepository.save(struttura);

            return true;
        } else return false;
    }

    // Set dati GENERALI STRUTTURA
    public void setDatiGeneraliStruttura(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                            Comune comune = comuneRepository.findByCodiceIstat(streamReader.getElementText()) != null ? comuneRepository.findByCodiceIstat(streamReader.getElementText()) : null;
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

            struttura.setAbilitato(true);
            Dizionario dizionario = dizionarioService.getDizionarioByCodifica(codTipologiaStruttura,"TIPOLOGIA_EDOTTO");
            if(dizionario != null) {
                struttura.setTipologiaEdotto(dizionario);
            }
        } finally {}
    }

    // Set ASL
    @Transactional
     public void setAsl(OrganigrammaStruttura asl, XMLStreamReader streamReader) throws XMLStreamException {
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
                            //codiceNSIS = streamReader.getElementText();
                            asl.setCodiceNSIS(streamReader.getElementText());
                            break;
                        case "email" :
                            //email = streamReader.getElementText();
                            asl.setEmailTitolare(streamReader.getElementText());
                            break;
                        case "telefono" :
                            //telefono = streamReader.getElementText();
                            asl.setTelefonoTitolare(streamReader.getElementText());
                            break;
                        case "partitaIVA" :
                            //partitaIVA = streamReader.getElementText();
                            asl.setCodice(streamReader.getElementText());
                            break;
                        case "codTipologiaGiuridica" :
                            codifica = streamReader.getElementText();
                            break;
                    }
                }
            } while (evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria"));
            //setAudit(asl);
            //asl.setAsl(true);

            if(codifica != null && !(codifica.isBlank())) {
                Dizionario dizionario = dizionarioService.getDizionarioByCodifica(codifica,"TIPO_GIURIDICA");

                if(dizionario != null) {
                    asl.setTipologiaEdotto(dizionario);
                }
            }
        } finally {}
    }

    // Set AUDIT
    public void setAudit(OrganigrammaStruttura struttura, HttpServletRequest request) {
        UtenteDto utenteInSessione = utenteService.getUtenteInSessione(request);

        if(struttura.getId() < 1) {
            struttura.setCreatedDate(LocalDate.now());
            struttura.setCreatedBy((int) utenteInSessione.getId());
            //todo struttura.setCreatedWith();
        } else {
            struttura.setModifiedDate(LocalDate.now());
            struttura.setModifiedBy((int) utenteInSessione.getId());
            //todo struttura.setModifiedWith();
        }
    }

    // Set DISTRETTO SOCIO-SANITARIO
    public void setDistretto(OrganigrammaStruttura distretto, XMLStreamReader streamReader) throws XMLStreamException {
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
                            OrganigrammaStruttura asl = findAslByCodice(streamReader.getElementText().trim()) != null ? findAslByCodice(streamReader.getElementText().trim()) : null;
                            distretto.setAsl(asl);
                            distretto.setParent(distretto.getAsl());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

            //setAudit(distretto);
            if(codifica != null && !(codifica.isBlank())) {
                distretto.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(codifica, "TIPO_GIURIDICA"));
            }
        } finally {}
    }

    public void setStruttura(OrganigrammaStruttura struttura, XMLStreamReader streamReader) {

    }

    // Set TIPOLOGIA ISTITUTO DI RICOVERO
    public void setTipoIstitutoDiRicovero( OrganigrammaStruttura struttura, String tipoIstituto) {
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
    public void setDatiIstitutoDiRicovero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                        case "datiFarmacia" :

                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(struttura.getTipologiaEdotto().getId() != 2 && struttura.getTipologiaEdotto().getId() != 3) {
                struttura.setParent(struttura.getAsl());
            }
        } finally {}
    }

    // Set dati FARMACIA
    public void setDatiFarmacia(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(progDistretto != null) {
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl().getId()));
            }
        } finally {}
    }

    // Set dati AMBULATORIO SPECIALISTICO
    public void setDatiAmbulatorioSpecialstico(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
    public void setDatiStrutturaHospice(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getAsl());
        } finally {}
    }

    // Set dati ISTITUTO PENITENZIARIO
    public void setDatiIstitutoPenitenziario(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    if(tag.equals("codiceMinisteriale")) {
                        struttura.setCodMinisteriale(streamReader.getElementText());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati OSPEDALE DI COMUNITA'
    public void setDatiOspedaleDiComunita(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    if(tag.equals("codiceNSIS")) {
                        struttura.setCodiceNSIS(streamReader.getElementText());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati POSTAZIONE EMERGENZA SANITARIA
    public void setDatiPostazioneEmergenzaSanitaria(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("progDistretto")) {
                        progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                        struttura.setProgDistretto(progDistretto);
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
    public void setDatiStrutturaSpecialistica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

                    if (tag.equals("codiceSTS11")) {
                        struttura.setCodiceSts11(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            struttura.setParent(struttura.getAsl());

        } finally {}
    }

    // Set dati PUNTO DI CONTINUITA' ASSISTENZIALE
    public void setDatiPuntoDiContinuitaAssistenziale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("progDistretto")) {
                        progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                        struttura.setProgDistretto(progDistretto);
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
    public void setDatiRepartoOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStrutturaIstitutoDiRicovero != null && progStabilimento != null) {
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStrutturaIstitutoDiRicovero, progStabilimento));
            }

        } finally {}
    }

    // Set dati SEDE MEDICINA DEI SERVIZI
    public void setDatiSedeMedicinaDeiServizi(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer progDistretto = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("progDistretto")) {
                        progDistretto = Integer.parseInt(streamReader.getElementText().trim());
                        struttura.setProgDistretto(progDistretto);
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
    public void setDatiServizioTerritorialeDipendenzaPatologica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("codiceSTS11")) {
                        struttura.setCodiceSts11(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

        } finally {}
    }

    // Set dati SERVIZIO TERRITORIALE PREVENZIONE
    public void setServizioTerritorialePrevenzione( OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("codiceNSIS")) {
                        struttura.setCodiceNSIS(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
        } finally {}
    }

    // Set dati SERVIZIO TERRITORIALE SALUTE MENTALE
    public void setDatiServizioTerritorialeSaluteMentale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("codiceSTS11")) {
                        struttura.setCodiceSts11(streamReader.getElementText().trim());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
        } finally {}
    }

    // Set dati SERVIZIO OSPEDALIERO
    public void setDatiServizioOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStruttIstitutoDaRicovero != null && progStabilimento != null) {
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStruttIstitutoDaRicovero, progStabilimento));
            }

        } finally {}
    }

    // Set dati RESIDENZA ASSISTENZIALE
    public void setDatiResidenzaAssistenziale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
    public void setDatiStabilimentoOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        Integer codStruttura = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    switch (tag) {
                        case "progStabilimento" :
                            struttura.setProgStabilimento(Integer.parseInt(streamReader.getElementText().trim()));
                            break;
                        case "codStrutturaIstitutoDiRicovero" :
                            codStruttura = Integer.parseInt(streamReader.getElementText());
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codStruttura != null) {
                struttura.setParent(findOneByCodiceEdotto(codStruttura));
            }

        } finally {}
    }

    // Set dati STRUTTURA RIABILITATIVA PSICHIATRICA
    public void setDatiStrutturaRiabilitativaPsichiatrica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
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
    public void setDatiStrutturaCureTermali(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equals("codiceSTS11")) {
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

    public OrganigrammaStruttura findAslByCodice(String codNSIS) {
        return orgRepositoryImpl.findAslByCodiceNSIS(codNSIS);
    }

    public OrganigrammaStruttura getStrutturaByCodiceEdottoIstitutoAndStabilimento(int codiceEdottoIstituto, int stabilimento) {
        return orgRepositoryImpl.findOneByCodiceEdottoGrampaAndStabilimento(codiceEdottoIstituto, stabilimento);
    }

    public OrganigrammaStruttura findOneByCodiceEdotto(int codiceEdotto) {
        return orgRepositoryImpl.findOneByCodiceEdotto(codiceEdotto);
    }

    public OrganigrammaStruttura getDistrettoByProg(int prog, Long idAsl) {
        return orgRepositoryImpl.findDistretto(prog, idAsl);
    }

    public OrganigrammaStruttura findOneByCodiceEdottoOfOriginal(int codiceEdotto) {
        return orgRepositoryImpl.findOneByCodiceEdottoOfOriginal(codiceEdotto);
    }

    public void identificaAndElaboraStruttura(OrganigrammaStruttura struttura, Path xmlStruttura) throws XMLStreamException {
        try (BufferedReader reader = new BufferedReader(new FileReader(xmlStruttura.toFile()))) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

            int evento;
            String tag = "";

            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

                    if(tag.equalsIgnoreCase("datiFarmacia")) {
                        if(struttura.getAsl() == null) {
                            throw new IOException("ERRORE ASL");
                        }

                    }
                }


            } while(streamReader.hasNext());

        } catch (XMLStreamException | IOException e) {
            throw new XMLStreamException(e.getMessage());
        }
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
