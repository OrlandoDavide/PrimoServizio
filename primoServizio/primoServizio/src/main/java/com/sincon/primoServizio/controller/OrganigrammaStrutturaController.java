package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.service.DizionarioService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@RestController
@RequestMapping("/strutture")
public class OrganigrammaStrutturaController {

    //private XMLConfig xmlConfig;
    private final String edottoPath = System.getProperty("user.home") + "/Desktop/ANAGRAFE_STRUTTURE_SANITARIE";
    private final StruttureEdottoService edottoService;
    private final DizionarioService dizionarioService;
    private static final Logger logger = LoggerFactory.getLogger(OrganigrammaStrutturaController.class);
    private Pattern XML_PATTERN;
    private List<String> fileElaborati = new ArrayList<>();

    @Autowired
    public OrganigrammaStrutturaController(StruttureEdottoService edottoService, DizionarioService dizionarioService) {
        this.edottoService = edottoService;
        this.dizionarioService = dizionarioService;
    }

//    @PostConstruct
//    public void initPathConfig() {
//        this.pathXML = System.getProperty("user.home") + xmlConfig.getPath();
//        this.XML_PATTERN =  Pattern.compile(xmlConfig.getPattern(), Pattern.CASE_INSENSITIVE);
//    }

    @GetMapping("/lista-file")
    public List<String> getNomeFileEdotto() {
        Path path = Paths.get(edottoPath);
        List<String> listaFile = new ArrayList<>();

        try (Stream<Path> paths = Files.list(path)) {
            listaFile = paths.filter(Files::isRegularFile)
                    .map(file -> file.getFileName().toString())
                    .filter(name -> name.endsWith(".xml"))
                    .map(name -> name.substring(0, name.length() - 4))
                    .toList();
        } catch (IOException e) {
            logger.error("Errore localizzato in ' getNomeFileEdotto() ' :\n" + e.getMessage());
        }
        return listaFile;
    }

    // Aggiorna EDOTTO
    @GetMapping("/aggiorna-edotto")
    public void aggiornaEdotto() throws IOException, XMLStreamException {
        //todo controllo indirizzo IP
        Path path = Paths.get(edottoPath);

        logger.info("Allineamento Strutture EDOTTO");

        if (Files.exists(path)) {
            Optional<Path> xmlAsl = edottoService.trovaXmlByTipologiaStruttura(path, "13"); //todo better

            if (xmlAsl.isPresent()) {
                fileElaborati.add(edottoService.getFileName(xmlAsl));

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

                                OrganigrammaStrutturaDto asl = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(asl, streamReader);

                                OrganigrammaStrutturaDto aslEsistente = edottoService.findOneByCodiceEdotto(asl.getCodiceEdotto()) != null ? edottoService.findAslByCodice(String.valueOf(asl.getCodiceEdotto())) : null;
                                if(aslEsistente == null) {
                                    asl.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.setAsl(asl, streamReader);
                                    edottoService.salvaStruttura(asl);
                                } else {
                                    System.out.println("ATRIMENTI L'ELSE E' VUOTO");
                                    edottoService.setDatiGeneraliStruttura(aslEsistente, streamReader);
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
                fileElaborati.add(edottoService.getFileName(xmlDistretti));
                logger.debug("Allineamento DISTRETTI SOCIO SANITARI");

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
                                OrganigrammaStrutturaDto distretto = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(distretto, streamReader);

                                OrganigrammaStrutturaDto distrettoEsistente = edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) : null;
                                if(distrettoEsistente == null) {
                                    distretto.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.setDistretto(distretto, streamReader);
                                    edottoService.salvaStruttura(distretto);
                                } else {
                                    edottoService.setDatiGeneraliStruttura(distrettoEsistente, streamReader);
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
                fileElaborati.add(edottoService.getFileName(xmlAsl));
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
                                OrganigrammaStrutturaDto strRicovero = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(strRicovero, streamReader);

                                OrganigrammaStrutturaDto strutturaEsistente = edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) : null;
                                if(strutturaEsistente == null) {
                                    strRicovero.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.setDatiIstitutoDiRicovero(strRicovero, streamReader);
                                    edottoService.salvaStruttura(strRicovero);
                                } else {
                                    edottoService.setDatiGeneraliStruttura(strutturaEsistente, streamReader);
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
                fileElaborati.add(edottoService.getFileName(xmlStabOspedaliero));
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
                                OrganigrammaStrutturaDto stabOspedaliero = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(stabOspedaliero, streamReader);

                                OrganigrammaStrutturaDto stabilimentoEsistente = edottoService.findOneByCodiceEdotto(stabOspedaliero.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(stabOspedaliero.getCodiceEdotto()) : null;
                                if(stabilimentoEsistente == null) {
                                    stabOspedaliero.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.identificaAndElaboraStruttura(stabOspedaliero, streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(stabOspedaliero);
                                } else {
                                    edottoService.setDatiGeneraliStruttura(stabilimentoEsistente, streamReader);
                                    edottoService.identificaAndElaboraStruttura(stabilimentoEsistente,  streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(stabilimentoEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    streamReader.close();
                    logger.debug("Fine Allineamento Stabilimenti Ospedalieri");

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'STABILIMENTO OSPEDALIERO'");
            }

            Optional<Path> xmlRepartoOsp = edottoService.trovaXmlByTipologiaStruttura(path, "5");
            if(xmlRepartoOsp.isPresent()) {
                fileElaborati.add(edottoService.getFileName(xmlRepartoOsp));
                logger.debug("Allineamento Reparti e Servizi Ospedalieri");

                try(BufferedReader reader = new BufferedReader(new FileReader(xmlRepartoOsp.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if(evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                                OrganigrammaStrutturaDto repartoOsp = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(repartoOsp, streamReader);

                                OrganigrammaStrutturaDto repartoEsistente = edottoService.findOneByCodiceEdotto(repartoOsp.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(repartoOsp.getCodiceEdotto()) : null;
                                if(repartoEsistente == null) {
                                    repartoOsp.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.identificaAndElaboraStruttura(repartoOsp, streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(repartoOsp);
                                } else {
                                    edottoService.setDatiGeneraliStruttura(repartoEsistente, streamReader);
                                    edottoService.identificaAndElaboraStruttura(repartoEsistente,  streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(repartoEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    streamReader.close();

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'REPARTO OSPEDALIERO'");
            }

            Optional<Path> xmlServizioOsp = edottoService.trovaXmlByTipologiaStruttura(path, "16");
            if(xmlServizioOsp.isPresent()) {
                fileElaborati.add(edottoService.getFileName(xmlServizioOsp));

                try(BufferedReader reader = new BufferedReader(new FileReader(xmlServizioOsp.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if(evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
                                OrganigrammaStrutturaDto servizioOsp = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(servizioOsp, streamReader);

                                OrganigrammaStrutturaDto servizioEsistente = edottoService.findOneByCodiceEdotto(servizioOsp.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(servizioOsp.getCodiceEdotto()) : null;
                                if(servizioEsistente == null) {
                                    servizioOsp.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.identificaAndElaboraStruttura(servizioOsp, streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(servizioOsp);
                                } else {
                                    edottoService.setDatiGeneraliStruttura(servizioEsistente, streamReader);
                                    edottoService.identificaAndElaboraStruttura(servizioEsistente,  streamReader, xmlStabOspedaliero);
                                    edottoService.salvaStruttura(servizioEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    logger.debug("Fine Allineamento Reparti e Servizi Ospedalieri");
                    streamReader.close();

                } catch (XMLStreamException e) {
                    throw new XMLStreamException(e.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'SERVIZIO OSPEDALIERO'");
            }




        } else {
            logger.error("Il percorso fornito non esiste.");
        }
    }
}
