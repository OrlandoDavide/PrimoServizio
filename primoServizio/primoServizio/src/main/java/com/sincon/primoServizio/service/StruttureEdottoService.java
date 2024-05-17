package com.sincon.primoServizio.service;

import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.model.Utente;
import com.sincon.primoServizio.repository.ComuneRepository;
import com.sincon.primoServizio.repository.DizionarioRepository;
import com.sincon.primoServizio.repository.OrganigrammaStrutturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class StruttureEdottoService {

    private final OrganigrammaStrutturaRepository organigrammaRepository;
    private final ComuneRepository comuneRepository;
    private final DizionarioRepository dizionarioRepository;
    private Utente utenteInSessione;


    @Autowired
    public StruttureEdottoService(OrganigrammaStrutturaRepository organigrammaRepository,
                                  ComuneRepository comuneRepository,
                                  DizionarioRepository dizionarioRepository )
    {
        this.organigrammaRepository = organigrammaRepository;
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
    }

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
        }
    }

    public void setAudit(OrganigrammaStruttura struttura) {
        if(struttura.getId() == null || struttura.getId() < 1) {
            struttura.setCreatedDate(LocalDate.now());
            struttura.setCreatedBy(Math.toIntExact(utenteInSessione.getId()));
            //struttura.setCreatedWith(); boh?
        } else {
            struttura.setModifiedDate(LocalDate.now());
            struttura.setModifiedBy(Math.toIntExact(utenteInSessione.getId()));
            //struttura.setModifiedWith(); boh?
        }
    }

    public void setDatiFarmacia(OrganigrammaStruttura struttura, XMLStreamReader streamReader) throws XMLStreamException {
        //struttura.setDistretto(); ??
        while (streamReader.hasNext()) {
            String tag = streamReader.getLocalName();
            switch (tag) {

            }
        }
    }
}
