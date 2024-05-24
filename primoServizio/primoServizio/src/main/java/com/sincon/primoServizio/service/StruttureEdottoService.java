package com.sincon.primoServizio.service;

import com.sincon.primoServizio.model.Dizionario;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.repository.ComuneRepository;
import com.sincon.primoServizio.repository.DizionarioRepositoryImpl;
import com.sincon.primoServizio.repository.OrgStrutturaRepositoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class StruttureEdottoService {
    private static final Logger logger = LoggerFactory.getLogger(StruttureEdottoService.class);
    private final OrgStrutturaRepositoryImpl orgRepositoryImpl;
    private final ComuneRepository comuneRepository;
    private final DizionarioService dizionarioService;
    private Utente utenteInSessione;

    @Autowired
    public StruttureEdottoService(OrgStrutturaRepositoryImpl orgRepositoryImpl,
                                  ComuneRepository comuneRepository,
                                  DizionarioService dizionarioService)
    {
        this.orgRepositoryImpl = orgRepositoryImpl;
        this.comuneRepository = comuneRepository;
        this.dizionarioService = dizionarioService;
    }

    // Set dati GENERALI STRUTTURA
    public void setDatiGeneraliStruttura(OrganigrammaStruttura struttura, XMLStreamReader datiGeneraliStruttura) throws XMLStreamException {
        String codTipologiaStruttura = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = datiGeneraliStruttura.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = datiGeneraliStruttura.getLocalName();

                    switch (tag) {
                        case "denominazione" :
                            struttura.setDenominazione(datiGeneraliStruttura.getElementText());
                            break;
                        case "codCAP" :
                            struttura.setCodCap(datiGeneraliStruttura.getElementText());
                            break;
                        case "comune" :
                            if(comuneRepository.findByCodiceIstat(datiGeneraliStruttura.getElementText()) != null) {
                                struttura.setComune(datiGeneraliStruttura.getElementText());
                            } else {
                                struttura.setComune(null);
                            }
                            break;
                        case "codTipologiaStruttura" :
                            codTipologiaStruttura = datiGeneraliStruttura.getElementText();
                            break;
                        case "dataIstituzione" :
                            LocalDate dataAttivazione = parseStringToLocalDate(datiGeneraliStruttura.getElementText());

                            struttura.setDataAttivazione(dataAttivazione);
                            struttura.setDataApertura(dataAttivazione);
                            break;
                        case "dataChiusura" :
                            LocalDate dataChiusura = parseStringToLocalDate(datiGeneraliStruttura.getElementText());

                            struttura.setDataChiusura(dataChiusura);
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(codTipologiaStruttura != null && !(codTipologiaStruttura.isBlank())) {
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioByCodifica(codTipologiaStruttura,
                                                                                "TIPOLOGIA_EDOTTO").getId());
            }
        } finally {  }
    }

    // Set ASL
    @Transactional
     public void setAsl(OrganigrammaStruttura asl, XMLStreamReader streamReader) throws XMLStreamException {
        String codifica = null;
        int evento;
        String tag = "";

        try {
            do {
                evento = streamReader.next();

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName();

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
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            setAudit(asl);
            setDatiGeneraliStruttura(asl, streamReader);

            if(codifica != null && !(codifica.isBlank())) {
                asl.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(codifica,
                                                                            "TIPO_GIURIDICA").getId());
            }
        } finally {  }
    }

    // Set AUDIT
    public void setAudit(OrganigrammaStruttura struttura) {
        if(struttura.getId() < 1) {
            struttura.setCreatedDate(LocalDate.now());
            struttura.setCreatedBy(Math.toIntExact(utenteInSessione.getId()));
            //todo struttura.setCreatedWith();
        } else {
            struttura.setModifiedDate(LocalDate.now());
            struttura.setModifiedBy(Math.toIntExact(utenteInSessione.getId()));
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

                if(evento == XMLStreamReader.START_ELEMENT) {
                    tag = streamReader.getLocalName().trim();

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
                            distretto.setAsl(findAslByCodice(streamReader.getElementText().trim()));
                            distretto.setParent(distretto.getAsl());
                            break;
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));

            setAudit(distretto);
            setDatiGeneraliStruttura(distretto, streamReader);
            if(codifica != null && !(codifica.isBlank())) {
                distretto.setTipologiaGiuridica(dizionarioService.getDizionarioByCodifica(codifica,
                                                                                  "TIPO_GIURIDICA").getId());
            }
        } finally {  }
    }

    // Set TIPOLOGIA ISTITUTO DI RICOVERO
    public void setTipoIstitutoDiRicovero( OrganigrammaStruttura struttura, String tipoIstituto) {
        switch (tipoIstituto) {
            case "1":
                // Presidio ospedaliero
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24023).getId());
                break;
            case "2":
                // IRCCS pubblico
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24028).getId());
                break;
            case "3":
                // Azienda ospedaliera
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24029).getId());
                break;
            case "4":
                // Casa si cura privata accreditata
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24024).getId());
                break;
            case "5":
                // Casa di cura privata NON accreditata
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24025).getId());
                break;
            case "6":
                // Ente ecclesiastico
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24026).getId());
                break;
            case "7":
                // IRCCS privato
                struttura.setTipologiaEdotto(dizionarioService.getDizionarioById(24027).getId());
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
                    }
                }
            } while (!(evento == XMLStreamReader.END_ELEMENT && tag.equalsIgnoreCase("datiStrutturaSanitaria")));
            if(struttura.getTipologiaEdotto() != 2 && struttura.getTipologiaEdotto() != 3) {
                struttura.setParent(struttura.getAsl());
            }
        } finally {  }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }
        } finally {  }
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
               struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
           }
       } finally {  }
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
        } finally { }
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

        } finally {  }
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

        } finally { }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }
        } finally {  }
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

        } finally { }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }

        } finally { }
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
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStrutturaIstitutoDiRicovero, progStabilimento).getId());
            }

        } finally { }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }
            struttura.setParent(struttura.getDistretto());

        } finally {    }
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

        } finally { }
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
        } finally { }
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
        } finally {  }
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
                struttura.setParent(getStrutturaByCodiceEdottoIstitutoAndStabilimento(codStruttIstitutoDaRicovero, progStabilimento).getId());
            }

        } finally { }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }

        } finally {  }
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
                struttura.setParent(getOneByCodiceEdotto(codStruttura).getId());
            }

        } finally {  }
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
                struttura.setDistretto(getDistrettoByProg(progDistretto, struttura.getAsl()).getId());
            }
        } finally {  }
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

        } finally { }
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

    public int findAslByCodice(String codNSIS) {
        return orgRepositoryImpl.findAslByCodiceNSIS(codNSIS).getId();
    }

    public OrganigrammaStruttura getStrutturaByCodiceEdottoIstitutoAndStabilimento(int codiceEdottoIstituto, int stabilimento) {
        return orgRepositoryImpl.findOneByCodiceEdottoGrampaAndStabilimento(codiceEdottoIstituto, stabilimento);
    }

    public OrganigrammaStruttura getOneByCodiceEdotto(int codiceEdotto) {
        return orgRepositoryImpl.findOneByCodiceEdotto(codiceEdotto);
    }

    public OrganigrammaStruttura getDistrettoByProg(int prog, int idAsl) {
        return orgRepositoryImpl.findDistretto(prog, idAsl);
    }

    public OrganigrammaStruttura findOneByCodiceEdottoOfOriginal(int codiceEdotto) {
        return orgRepositoryImpl.findOneByCodiceEdottoOfOriginal(codiceEdotto);
    }
}
