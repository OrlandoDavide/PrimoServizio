package com.sincon.primoServizio.service;

import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.repository.ComuneRepository;
import com.sincon.primoServizio.repository.DizionarioRepository;
import com.sincon.primoServizio.repository.OrgStrutturaRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class StruttureEdottoService {

    private final OrgStrutturaRepositoryImpl orgRepositoryImpl;
    private final ComuneRepository comuneRepository;
    private final DizionarioRepository dizionarioRepository;
    private Utente utenteInSessione;


    @Autowired
    public StruttureEdottoService(OrgStrutturaRepositoryImpl orgRepositoryImpl,
                                  ComuneRepository comuneRepository,
                                  DizionarioRepository dizionarioRepository )
    {
        this.orgRepositoryImpl = orgRepositoryImpl;
        this.comuneRepository = comuneRepository;
        this.dizionarioRepository = dizionarioRepository;
    }


    // Cast da String a LocalDate, format: "yyyy-MM-dd"
    public LocalDate parseStringToLocalDate(String stringData) throws ParseException {
         DateTimeFormatter formatoData = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        return LocalDate.parse(stringData, formatoData);
    }

    // Verifica se l'indirizzo IP è "locale"
    public boolean isLocalAddress(String indirizzoIP) {
        //todo controlla se indirizzo è locale
        return false;
    }

    // Set dati GENERALI STRUTTURA
    public void setDatiGeneraliStruttura(OrganigrammaStruttura struttura, XMLStreamReader datiGeneraliStruttura) throws XMLStreamException, ParseException {
        while (datiGeneraliStruttura.hasNext()) {
            String tag = datiGeneraliStruttura.getLocalName();
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
                    // todo set tipologiaEdotto
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
                default:
                    throw new IllegalStateException("Campo inatteso : " + tag);
            }
        }
        datiGeneraliStruttura.close();
    }

    // Set ASL
    @Transactional
     public void setASL(OrganigrammaStruttura asl, XMLStreamReader streamReader) throws XMLStreamException, ParseException {
        setAudit(asl);
        setDatiGeneraliStruttura(asl, streamReader);
        while (streamReader.hasNext()) {
            int evento = streamReader.next();
            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                switch (tag) {
                    case "datiGeneraliStruttura":

                }
            }
        }
        streamReader.close();
    }

    // Aggiorna EDOTTO
    @Transactional
    public void aggiornaEdotto(String xmlFilePath) throws IOException, XMLStreamException {
        try(BufferedReader reader = new BufferedReader(new FileReader(xmlFilePath + "ASL.xml"))) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

            while(streamReader.hasNext()) {
                int tag = streamReader.next();
                if(tag == XMLStreamReader.START_ELEMENT && streamReader.getLocalName().equals("datiStruttutaSanitaria")) {
//                    String codiceEdotto = streamReader.getAttributeLocalName()
//                    OrganigrammaStruttura asl = organigrammaRepository.findByCodiceEdotto()
                }
            }
            streamReader.close();
        }
    }

    // Set AUDIT
    public void setAudit(OrganigrammaStruttura struttura) {
        if(struttura.getId() == null || struttura.getId() < 1) {
            struttura.setCreatedDate(LocalDate.now());
            struttura.setCreatedBy(Math.toIntExact(utenteInSessione.getId()));
            //todo struttura.setCreatedWith(); boh?
        } else {
            struttura.setModifiedDate(LocalDate.now());
            struttura.setModifiedBy(Math.toIntExact(utenteInSessione.getId()));
            //todo struttura.setModifiedWith(); boh?
        }
    }

    // Set DISTRETTO SOCIO-SANITARIO
    public void setDistretto(OrganigrammaStruttura distretto, XMLStreamReader streamReader) throws XMLStreamException, ParseException {
        setAudit(distretto);
        setDatiGeneraliStruttura(distretto, streamReader);

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName().trim();

                switch (tag) {
                    case "partitaIVA" :
                        distretto.setCodice(streamReader.getElementText().trim());
                        break;
                    case "progDistretto" :
                       distretto.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()
                       ));
                       break;
                    case "codTipologiaGiuridica" :
                        //todo;
                        break;
                    case "email" :
                        distretto.setEmailTitolare(streamReader.getElementText().trim());
                        break;
                    case "telefono" :
                        distretto.setTelefonoTitolare(streamReader.getElementText().trim());
                        break;
                    case "codNazionaleASLAppartenenzaTerritoriale" :
                        distretto.setAsl(Integer.parseInt(orgRepositoryImpl.findAslByCodiceNSIS(streamReader.getElementText())));
                        distretto.setParent(distretto.getAsl());
                        break;
                }
            }
        }
        streamReader.close();
    }

    // Set dati FARMACIA
    public void setDatiFarmacia(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();
                if (tag.equals("codProvinciale")) {
                    struttura.setCodProvinciale(streamReader.getElementText());
                }
            }
        }
        streamReader.close();
    }

    // Set dati ISTITUTO PENITENZIARIO
    public void setDatiIstitutoPenitenziario(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName().trim();
                if(tag.equals("codiceMinisteriale")) {
                    struttura.setCodMinisteriale(streamReader.getElementText());
                }
            }
        }
        streamReader.close();
    }

    // Set dati OSPEDALE DI COMUNITA'
    public void setDatiOspedaleDiComunita(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName().trim();
                if(tag.equals("codiceNSIS")) {
                    struttura.setCodiceNSIS(streamReader.getElementText());
                }
            }
        }
        streamReader.close();
    }

    // Set dati POSTAZIONE EMERGENZA SANITARIA
    public void setDatiPostazioneEmergenzaSanitaria(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();
        struttura.setParent(struttura.getDistretto());

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();
                if(tag.equals("progDistretto")) {
                    struttura.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()));
                }
            }
        }
        streamReader.close();
    }

    // Set dati STRUTTURA SPECIALISTICA
    public void setDatiStrutturaSpecialistica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName().trim();
                if (tag.equals("codiceSTS11")) {
                    struttura.setCodiceSts11(streamReader.getElementText().trim());
                    struttura.setParent(struttura.getAsl());
                }
            }
        }
        streamReader.close();
    }

    // Set dati PUNTO DI CONTINUITA' ASSISTENZIALE
    public void setDatiPuntoDiContinuitaAssistenziale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();
        struttura.setParent(struttura.getDistretto());

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();
                if(tag.equals("progDistretto")) {
                    struttura.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()));
                }
            }
        }
        streamReader.close();
    }

    // Set dati REPARTO OSPEDALIERO
    public void setDatiRepartiOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setParent();

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                switch (tag) {
                    case "codiceHSP12" :
                        struttura.setCodiceHsp12(streamReader.getElementText().trim());
                        break;
                    case "progStabilimento" :
                        struttura.setProgStabilimento(Integer.parseInt(streamReader.getElementText().trim()));
                        break;
                    case "progReparto" :
                        struttura.setProgReparto(Integer.parseInt(streamReader.getElementText().trim()));
                        break;
                }
            }
        }
        streamReader.close();
    }

    // Set dati SEDE MEDICINA DEI SERVIZI
    public void setDatiSedeMedicinaDeiServizi(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();
        struttura.setParent(struttura.getDistretto());

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if(tag.equals("progDistretto")) {
                    struttura.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()));
                }
            }
        }
        streamReader.close();
    }

    // Set dati SERVIZIO TERRITORIALE DIPENDENZA PATOLOGICA
    public void setDatiServizioTerritorialeDipendenzaPatologica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if(tag.equals("codiceSTS11")) {
                    struttura.setCodiceSts11(streamReader.getElementText().trim());
                }
            }
        }
        streamReader.close();
    }

    // Set dati SERVIZIO TERRITORIALE PREVENZIONE
    public void setServizioTerritorialePrevenzione( OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if(tag.equals("codiceNSIS")) {
                    struttura.setCodiceNSIS(streamReader.getElementText().trim());
                }
            }
        }
        streamReader.close();
    }

    // Set dati SERVIZIO TERRITORIALE SALUTE MENTALE
    public void setDatiServizioTerritorialeSaluteMentale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if(tag.equals("codiceSTS11")) {
                    struttura.setCodiceSts11(streamReader.getElementText().trim());
                }
            }
        }
        streamReader.close();
    }

    // Set dati SERVIZIO OSPEDALIERO
    public void setDatiServizioOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                switch (tag) {
                    case "codSpecialitaClinica" :
                        struttura.setCodiceSpecialitaClinica(streamReader.getElementText().trim());
                        break;
                    case "progServizio" :
                        struttura.setProgServizio(Integer.parseInt(streamReader.getElementText().trim()));
                        break;
                    case "progStabilimento" :
                        String progStabilimento = streamReader.getElementText().trim();
                        struttura.setProgStabilimento(Integer.parseInt(progStabilimento));
                        break;
                    case "codStrutturaIstitutoDiRicovero" :
                        String codStruttIstitutoDaRicovero = streamReader.getElementText();
                        break;
                }
            }
        }
        streamReader.close();
    }

    // Set dati RESIDENZA ASSISTENZIALE
    public void setDatiResidenzaAssistenziale(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();
        struttura.setParent(struttura.getDistretto());

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                switch (tag) {
                    case "codiceSTS11" :
                        struttura.setCodiceSts11(streamReader.getElementText().trim());
                        break;
                    case "progDistretto" :
                        struttura.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()));
                        break;
                }
            }
        }
        streamReader.close();
    }

    // Set dati STABILIMENTO OSPEDALIERO
    public void setDatiStabilimentoOspedaliero(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setParent();

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if (tag.equals("progStabilimento")) {
                    struttura.setProgStabilimento(Integer.parseInt(streamReader.getElementText().trim()));
                }
            }
        }
        streamReader.close();
    }

    // Set dati STRUTTURA RIABILITATIVA PSICHIATRICA
    public void setDatiStrutturaRiabilitativaPsichiatrica(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //todo struttura.setDistretto();
        struttura.setParent(struttura.getDistretto());

        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                switch (tag) {
                    case "codiceSTS11" :
                        struttura.setCodiceSts11(streamReader.getElementText().trim());
                        break;
                    case "progDistretto" :
                        struttura.setProgDistretto(Integer.parseInt(streamReader.getElementText().trim()));
                        break;
                }
            }
        }
        streamReader.close();
    }

    // Set dati STRUTTURA CURE TERMALI
    public void setDatiStrutturaCureTermali(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        while (streamReader.hasNext()) {
            int evento = streamReader.next();

            if(evento == XMLStreamReader.START_ELEMENT) {
                String tag = streamReader.getLocalName();

                if(tag.equals("codiceSTS11")) {
                    struttura.setCodiceSts11(streamReader.getElementText().trim());
                }
            }
        }
        streamReader.close();
    }

    public long findAslByCodice(String codNSIS) {
        return orgRepositoryImpl.findAslByCodiceNSIS(codNSIS).getId();
    }

    public long getStrutturaByCodiceEdottoIstitutoAndStabilimento(int codiceEdottoIstituto, int stabilimento) {
        return orgRepositoryImpl.findOneByCodiceEdottoGrampaAndStabilimento(codiceEdottoIstituto, stabilimento);
    }
}
