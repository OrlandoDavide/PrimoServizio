package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.service.StruttureEdottoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.stream.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/strutture")
public class OrganigrammaStrutturaController {
    //private XMLConfig xmlConfig;
    private final String edottoPath = System.getProperty("user.home") + "/Desktop/ANAGRAFE_STRUTTURE_SANITARIE";
    private final StruttureEdottoService edottoService;
    private static final Logger logger = LoggerFactory.getLogger(OrganigrammaStrutturaController.class);
    private Pattern XML_PATTERN;

    @Autowired
    public OrganigrammaStrutturaController(StruttureEdottoService edottoService) {
        this.edottoService = edottoService;
    }

//    @PostConstruct
//    public void initPathConfig() {
//        this.pathXML = System.getProperty("user.home") + xmlConfig.getPath();
//        this.XML_PATTERN =  Pattern.compile(xmlConfig.getPattern(), Pattern.CASE_INSENSITIVE);
//    }

    // Aggiorna EDOTTO
    @GetMapping("/aggiorna-edotto")
    public void aggiornaEdotto() throws IOException, XMLStreamException {
        //todo controllo indirizzo IP
        Path path = Paths.get(edottoPath);
        System.out.println(path);

        logger.info("Allineamento Strutture EDOTTO");

        if (Files.exists(path)) {
            Optional<Path> xmlAsl = edottoService.trovaXmlByTipologiaStruttura(path, "13"); //todo better

            if (xmlAsl.isPresent()) {
                logger.debug("Allineamento ASL");

                try (BufferedReader reader = new BufferedReader(new FileReader(xmlAsl.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
                    logger.info("HO CREATO IL READER DEL FILE");

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if (evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if (tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                StringWriter stringWriter = new StringWriter();
//                                XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();
//                                XMLStreamWriter streamWriter = outputFactory.createXMLStreamWriter(stringWriter);
//
//                                streamWriter.writeStartElement("datiStrutturaSanitaria");
//                                edottoService.racchiudiDatiSanitari(streamReader, streamWriter);
//
//                                streamWriter.flush();
//                                streamWriter.close();

//                                StringReader stringReader = new StringReader(stringWriter.toString());
//                                XMLStreamReader strutturaSanitariaReader = inputFactory.createXMLStreamReader(stringReader);

                                OrganigrammaStruttura asl = new OrganigrammaStruttura();
                                edottoService.setDatiGeneraliStruttura(asl, streamReader);

                                OrganigrammaStruttura aslEsistente = edottoService.findOneByCodiceEdotto(asl.getCodiceEdotto()) != null ? edottoService.findAslByCodice(String.valueOf(asl.getCodiceEdotto())) : null;
                                if(aslEsistente == null) {
                                    //TODO asl.setTipologia();
                                    edottoService.setAsl(asl, streamReader);
                                    edottoService.salvaStruttura(asl);
                                } else {
                                    System.out.println("ATRIMENTI L'ELSE E' VUOTO");
                                    //TODO setto aslEsistente con i dati dell'asl
                                    edottoService.setAsl(aslEsistente, streamReader);
                                    edottoService.salvaStruttura(aslEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    streamReader.close();
                    logger.debug("Fine Allineamento ASL");

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'ASL'");
            }

            Optional<Path> xmlDistretti = edottoService.trovaXmlByTipologiaStruttura(path, "12");
            if(xmlDistretti.isPresent()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(xmlDistretti.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if(evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                                OrganigrammaStruttura distretto = new OrganigrammaStruttura();
                                edottoService.setDatiGeneraliStruttura(distretto, streamReader);

                                OrganigrammaStruttura distrettoEsistente = edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) : null;
                                if(distrettoEsistente == null) {
                                    //TODO setTipologia();
                                    edottoService.setDistretto(distretto, streamReader);
                                    edottoService.salvaStruttura(distretto);
                                } else {
                                    System.out.println("ATRIMENTI L'ELSE E' VUOTO");
                                    //TODO setto distrettoEsistente con i dati del distretto
                                    edottoService.setDistretto(distrettoEsistente, streamReader);
                                    edottoService.salvaStruttura(distrettoEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    streamReader.close();
                    logger.debug("Fine Allineamento DISTRETTI SOCIO SANITARI");

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'DISTRETTO SOCIO SANITARIO'");
            }

            Optional<Path> xmlRicoveri = edottoService.trovaXmlByTipologiaStruttura(path, "2");
            if(xmlRicoveri.isPresent()) {
                logger.debug("Allineamento Strutture di Ricovero");

                try (BufferedReader reader = new BufferedReader(new FileReader(xmlRicoveri.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if(evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                                OrganigrammaStruttura strRicovero = new OrganigrammaStruttura();
                                edottoService.setDatiGeneraliStruttura(strRicovero, streamReader);

                                OrganigrammaStruttura strutturaEsistente = edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) : null;
                                if(strutturaEsistente == null) {
                                    //TODO strRicovero.setTipologia();
                                    edottoService.setDatiIstitutoDiRicovero(strRicovero, streamReader);
                                    edottoService.salvaStruttura(strRicovero);
                                } else {
                                    //TODO setto distrettoEsistente con i dati del distretto
                                    edottoService.setDatiIstitutoDiRicovero(strutturaEsistente, streamReader);
                                    edottoService.salvaStruttura(strutturaEsistente);
                                }
                            }
                        }

                    } while(streamReader.hasNext());

                    streamReader.close();
                    logger.debug("Fine Allineamento Strutture di Ricovero");

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'ISTITUTO DI RICOVERO'");
            }

            Optional<Path> xmlStabOspedaliero = edottoService.trovaXmlByTipologiaStruttura(path, "4");
            if(xmlStabOspedaliero.isPresent()) {
                logger.debug("Allineamento Stabilimenti Ospedalieri");

                try(BufferedReader reader = new BufferedReader(new FileReader(xmlStabOspedaliero.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if(evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                                OrganigrammaStruttura stabOspedaliero = new OrganigrammaStruttura();
                                edottoService.setDatiGeneraliStruttura(stabOspedaliero, streamReader);


                            }
                        }


                    } while (streamReader.hasNext());

                    streamReader.close();
                    logger.debug("Fine Allineamento Stabilimenti Ospedalieri");

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            }


        } else {
            logger.error("Il percorso fornito non esiste.");
        }
    }


}
