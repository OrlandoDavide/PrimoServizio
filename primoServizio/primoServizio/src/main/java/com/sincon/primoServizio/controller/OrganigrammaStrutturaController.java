package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.service.StruttureEdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Pattern;

@RestController
public class OrganigrammaStrutturaController {

    private static final Pattern XML_PATTERN = Pattern.compile(".*\\.xml$", Pattern.CASE_INSENSITIVE);
    private final StruttureEdottoService edottoService;

    @Autowired
    public OrganigrammaStrutturaController(StruttureEdottoService edottoService) {
        this.edottoService = edottoService;
    }

    // Aggiorna EDOTTO
    @Transactional
    public void aggiornaEdotto(String xmlFilePath) throws IOException, XMLStreamException {
        try(BufferedReader reader = new BufferedReader(new FileReader(xmlFilePath + "ASL.xml"))) {
            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
        }
    }
}
