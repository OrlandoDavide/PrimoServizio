package com.sincon.primoServizio.controller;

import com.sincon.primoServizio.dto.OrganigrammaStrutturaDto;
import com.sincon.primoServizio.mapperEntityDto.OrgStrutturaMapper;
import com.sincon.primoServizio.model.OrganigrammaStruttura;
import com.sincon.primoServizio.service.DizionarioService;
import com.sincon.primoServizio.service.StruttureEdottoService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    private final OrgStrutturaMapper edottoMapper;
    private final DizionarioService dizionarioService;
    private static final Logger logger = LoggerFactory.getLogger(OrganigrammaStrutturaController.class);
    private Pattern XML_PATTERN;
    private final List<String> fileElaborati = new ArrayList<>();

    @Autowired
    public OrganigrammaStrutturaController(StruttureEdottoService edottoService,
                                           OrgStrutturaMapper edottoMapper,
                                           DizionarioService dizionarioService) {
        this.edottoService = edottoService;
        this.edottoMapper = edottoMapper;
        this.dizionarioService = dizionarioService;
    }

//    @PostConstruct
//    public void initPathConfig() {
//        this.pathXML = System.getProperty("user.home") + xmlConfig.getPath();
//        this.XML_PATTERN =  Pattern.compile(xmlConfig.getPattern(), Pattern.CASE_INSENSITIVE);
//    }

    @GetMapping("/file-elaborati")
    public String listaFileElaborati() {
        return fileElaborati.toString();
    }

    @GetMapping("/lista-file")
    public List<String> getNomiFileEdotto() {
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
    public ResponseEntity<?> aggiornaEdotto() throws Exception {
        //todo controllo indirizzo IP
        Path path = Paths.get(edottoPath);

        logger.info("Allineamento Strutture EDOTTO");

        if (Files.exists(path)) {
            Optional<Path> xmlAsl = edottoService.trovaXmlByTipologiaStruttura(path, "13"); //todo better

            if (xmlAsl.isPresent()) {
                logger.info("Allineamento ASL");

                try (BufferedReader reader = new BufferedReader(new FileReader(xmlAsl.get().toFile()))) {
                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);

                    int evento;
                    String tag = "";

                    do {
                        evento = streamReader.next();

                        if (evento == XMLStreamReader.START_ELEMENT) {
                            tag = streamReader.getLocalName().trim();

                            if (tag.equalsIgnoreCase("datiStrutturaSanitaria")) {

                                OrganigrammaStrutturaDto asl = new OrganigrammaStrutturaDto();
                                edottoService.setDatiGeneraliStruttura(asl, streamReader);

                                OrganigrammaStrutturaDto aslEsistente = edottoService.findOneByCodiceEdotto(asl.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(asl.getCodiceEdotto()) : null;
                                if(aslEsistente == null) {
                                    asl.setTipologia(dizionarioService.getDizionario(90008));
                                    edottoService.setAsl(asl, streamReader);
                                    edottoService.salvaStruttura(asl);
                                } else {
                                    //edottoService.aggiornaStruttura(asl, aslEsistente);
                                    edottoService.salvaStruttura(aslEsistente);
                                    edottoService.setAsl(aslEsistente, streamReader);
                                    edottoService.salvaStruttura(aslEsistente);
                                }
                            }
                        }
                    } while (streamReader.hasNext());

                    fileElaborati.add(xmlAsl.get().getFileName().toString());
                    streamReader.close();

                    logger.info("Fine Allineamento ASL");

                } catch (XMLStreamException e) {
                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlAsl.get().getFileName().toString(), e.getMessage()));
                    throw new XMLStreamException(e.getMessage());
                } catch (Exception ex) {
                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlAsl.get().getFileName().toString(), ex.getMessage()));
                    throw new Exception(ex.getMessage());
                }
            } else {
                logger.error("Non è stato trovato nessun file per tipologia 'ASL'");
            }

//            Optional<Path> xmlDistretti = edottoService.trovaXmlByTipologiaStruttura(path, "12");
//            if(xmlDistretti.isPresent()) {
//                logger.info("Allineamento DISTRETTI SOCIO SANITARI");
//
//                try (BufferedReader reader = new BufferedReader(new FileReader(xmlDistretti.get().toFile()))) {
//                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                    int evento;
//                    String tag = "";
//
//                    do {
//                        evento = streamReader.next();
//
//                        if(evento == XMLStreamReader.START_ELEMENT) {
//                            tag = streamReader.getLocalName().trim();
//
//                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                OrganigrammaStrutturaDto distretto = new OrganigrammaStrutturaDto();
//                                edottoService.setDatiGeneraliStruttura(distretto, streamReader);
//
//                                OrganigrammaStrutturaDto distrettoEsistente = edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(distretto.getCodiceEdotto()) : null;
//                                if(distrettoEsistente == null) {
//                                    distretto.setTipologia(dizionarioService.getDizionario(90008));
//                                    edottoService.setDistretto(distretto, streamReader);
//                                    edottoService.salvaStruttura(distretto);
//                                } else {
//                                    edottoMapper.aggiornaStrutturaDto(distretto, distrettoEsistente);
//                                    edottoService.setDistretto(distrettoEsistente, streamReader);
//                                    edottoService.salvaStruttura(distrettoEsistente);
//                                }
//                            }
//                        }
//                    } while (streamReader.hasNext());
//
//                    fileElaborati.add(xmlDistretti.get().getFileName().toString());
//                    streamReader.close();
//                    logger.info("Fine Allineamento Distretti Socio Sanitari");
//
//                } catch (XMLStreamException e) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlDistretti.get().getFileName().toString(), e.getMessage()));
//                    throw new XMLStreamException(e.getMessage());
//                } catch (Exception ex) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlDistretti.get().getFileName().toString(), ex.getMessage()));
//                    throw new Exception(ex.getMessage());
//                }
//            } else {
//                logger.error("Non è stato trovato nessun file per tipologia 'DISTRETTO SOCIO SANITARIO'");
//            }
//
//            Optional<Path> xmlRicoveri = edottoService.trovaXmlByTipologiaStruttura(path, "2");
//            if(xmlRicoveri.isPresent()) {
//                logger.info("Allineamento Strutture di Ricovero");
//
//                try (BufferedReader reader = new BufferedReader(new FileReader(xmlRicoveri.get().toFile()))) {
//                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                    int evento;
//                    String tag = "";
//
//                    do {
//                        evento = streamReader.next();
//
//                        if(evento == XMLStreamReader.START_ELEMENT) {
//                            tag = streamReader.getLocalName().trim();
//
//                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                OrganigrammaStrutturaDto strRicovero = new OrganigrammaStrutturaDto();
//                                edottoService.setDatiGeneraliStruttura(strRicovero, streamReader);
//
//                                OrganigrammaStrutturaDto strutturaEsistente = edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(strRicovero.getCodiceEdotto()) : null;
//                                if(strutturaEsistente == null) {
//                                    strRicovero.setTipologia(dizionarioService.getDizionario(90008));
//                                    edottoService.setDatiIstitutoDiRicovero(strRicovero, streamReader);
//                                    edottoService.salvaStruttura(strRicovero);
//                                } else {
//                                    edottoMapper.aggiornaStrutturaDto(strRicovero, strutturaEsistente);
//                                    edottoService.setDatiIstitutoDiRicovero(strutturaEsistente, streamReader);
//                                    edottoService.salvaStruttura(strutturaEsistente);
//                                }
//                            }
//                        }
//                    } while(streamReader.hasNext());
//
//                    fileElaborati.add(xmlRicoveri.get().getFileName().toString());
//                    streamReader.close();
//                    logger.info("Fine Allineamento Strutture di Ricovero");
//
//                } catch (XMLStreamException e) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlRicoveri.get().getFileName().toString(), e.getMessage()));
//                    throw new XMLStreamException(e.getMessage());
//                } catch (Exception ex) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlRicoveri.get().getFileName().toString(), ex.getMessage()));
//                    throw new Exception(ex.getMessage());
//                }
//            } else {
//                logger.error("Non è stato trovato nessun file per tipologia 'ISTITUTO DI RICOVERO'");
//            }
//
//            Optional<Path> xmlStabOspedaliero = edottoService.trovaXmlByTipologiaStruttura(path, "4");
//            if(xmlStabOspedaliero.isPresent()) {
//                logger.info("Allineamento Stabilimenti Ospedalieri");
//
//                try(BufferedReader reader = new BufferedReader(new FileReader(xmlStabOspedaliero.get().toFile()))) {
//                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                    int evento;
//                    String tag = "";
//
//                    do {
//                        evento = streamReader.next();
//
//                        if(evento == XMLStreamReader.START_ELEMENT) {
//                            tag = streamReader.getLocalName().trim();
//
//                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                OrganigrammaStrutturaDto stabOspedaliero = new OrganigrammaStrutturaDto();
//                                edottoService.setDatiGeneraliStruttura(stabOspedaliero, streamReader);
//
//                                OrganigrammaStrutturaDto stabilimentoEsistente = edottoService.findOneByCodiceEdotto(stabOspedaliero.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(stabOspedaliero.getCodiceEdotto()) : null;
//                                if(stabilimentoEsistente == null) {
//                                    stabOspedaliero.setTipologia(dizionarioService.getDizionario(90008));
//                                    edottoService.identificaAndElaboraStruttura(stabOspedaliero, streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(stabOspedaliero);
//                                } else {
//                                    edottoMapper.aggiornaStrutturaDto(stabOspedaliero, stabilimentoEsistente);
//                                    edottoService.identificaAndElaboraStruttura(stabilimentoEsistente,  streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(stabilimentoEsistente);
//                                }
//                            }
//                        }
//                    } while (streamReader.hasNext());
//
//                    fileElaborati.add(xmlStabOspedaliero.get().getFileName().toString());
//                    streamReader.close();
//                    logger.info("Fine Allineamento Stabilimenti Ospedalieri");
//
//                } catch (XMLStreamException e) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlStabOspedaliero.get().getFileName().toString(), e.getMessage()));
//                    throw new XMLStreamException(e.getMessage());
//                } catch (Exception ex) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlStabOspedaliero.get().getFileName().toString(), ex.getMessage()));
//                    throw new Exception(ex.getMessage());
//                }
//            } else {
//                logger.error("Non è stato trovato nessun file per tipologia 'STABILIMENTO OSPEDALIERO'");
//            }
//
//            Optional<Path> xmlRepartoOsp = edottoService.trovaXmlByTipologiaStruttura(path, "5");
//            if(xmlRepartoOsp.isPresent()) {
//                logger.info("Allineamento Reparti e Servizi Ospedalieri");
//
//                try(BufferedReader reader = new BufferedReader(new FileReader(xmlRepartoOsp.get().toFile()))) {
//                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                    int evento;
//                    String tag = "";
//
//                    do {
//                        evento = streamReader.next();
//
//                        if(evento == XMLStreamReader.START_ELEMENT) {
//                            tag = streamReader.getLocalName().trim();
//
//                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                OrganigrammaStrutturaDto repartoOsp = new OrganigrammaStrutturaDto();
//                                edottoService.setDatiGeneraliStruttura(repartoOsp, streamReader);
//
//                                OrganigrammaStrutturaDto repartoEsistente = edottoService.findOneByCodiceEdotto(repartoOsp.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(repartoOsp.getCodiceEdotto()) : null;
//                                if(repartoEsistente == null) {
//                                    repartoOsp.setTipologia(dizionarioService.getDizionario(90008));
//                                    edottoService.identificaAndElaboraStruttura(repartoOsp, streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(repartoOsp);
//                                } else {
//                                    edottoMapper.aggiornaStrutturaDto(repartoOsp, repartoEsistente);
//                                    edottoService.identificaAndElaboraStruttura(repartoEsistente,  streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(repartoEsistente);
//                                }
//                            }
//                        }
//                    } while (streamReader.hasNext());
//
//                    fileElaborati.add(xmlRepartoOsp.get().getFileName().toString());
//                    streamReader.close();
//
//                } catch (XMLStreamException e) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlRepartoOsp.get().getFileName().toString(), e.getMessage()));
//                    throw new XMLStreamException(e.getMessage());
//                } catch (Exception ex) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlRepartoOsp.get().getFileName().toString(), ex.getMessage()));
//                    throw new Exception(ex.getMessage());
//                }
//            } else {
//                logger.error("Non è stato trovato nessun file per tipologia 'REPARTO OSPEDALIERO'");
//            }
//
//            Optional<Path> xmlServizioOsp = edottoService.trovaXmlByTipologiaStruttura(path, "16");
//            if(xmlServizioOsp.isPresent()) {
//
//                try(BufferedReader reader = new BufferedReader(new FileReader(xmlServizioOsp.get().toFile()))) {
//                    XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                    XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                    int evento;
//                    String tag = "";
//
//                    do {
//                        evento = streamReader.next();
//
//                        if(evento == XMLStreamReader.START_ELEMENT) {
//                            tag = streamReader.getLocalName().trim();
//
//                            if(tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                OrganigrammaStrutturaDto servizioOsp = new OrganigrammaStrutturaDto();
//                                edottoService.setDatiGeneraliStruttura(servizioOsp, streamReader);
//
//                                OrganigrammaStrutturaDto servizioEsistente = edottoService.findOneByCodiceEdotto(servizioOsp.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(servizioOsp.getCodiceEdotto()) : null;
//                                if(servizioEsistente == null) {
//                                    servizioOsp.setTipologia(dizionarioService.getDizionario(90008));
//                                    edottoService.identificaAndElaboraStruttura(servizioOsp, streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(servizioOsp);
//                                } else {
//                                    edottoMapper.aggiornaStrutturaDto(servizioOsp, servizioEsistente);
//                                    edottoService.identificaAndElaboraStruttura(servizioEsistente,  streamReader, xmlStabOspedaliero);
//                                    edottoService.salvaStruttura(servizioEsistente);
//                                }
//                            }
//                        }
//                    } while (streamReader.hasNext());
//
//                    fileElaborati.add(xmlServizioOsp.get().getFileName().toString());
//                    logger.info(fileElaborati.toString());
//                    logger.info("Fine Allineamento Reparti e Servizi Ospedalieri");
//                    streamReader.close();
//
//                } catch (XMLStreamException e) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlServizioOsp.get().getFileName().toString(), e.getMessage()));
//                    throw new XMLStreamException(e.getMessage());
//                } catch (Exception ex) {
//                    logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", xmlServizioOsp.get().getFileName().toString(), ex.getMessage()));
//                    throw new Exception(ex.getMessage());
//                }
//            } else {
//                logger.error("Non è stato trovato nessun file per tipologia 'SERVIZIO OSPEDALIERO'");
//            }

//            logger.info("Allineamento strutture");
//            try {
//                Files.list(path)
//                        .filter(Files::isRegularFile)
//                        .forEach(file -> {
//                            String nomeFile = file.getFileName().toString();
//                            if (!(fileElaborati.contains(nomeFile)) && file.endsWith(".xml")) {
//                                logger.info(String.format("Allineamento %s", edottoService.getNomeFile(nomeFile)));
//
//
//                            try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
//                                XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                                XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                                int evento;
//                                String tag = "";
//
//                                do {
//                                    evento = streamReader.next();
//
//                                    if (evento == XMLStreamReader.START_ELEMENT) {
//                                        tag = streamReader.getLocalName().trim();
//
//                                        if (tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                            OrganigrammaStrutturaDto struttura = new OrganigrammaStrutturaDto();
//                                            edottoService.setDatiGeneraliStruttura(struttura, streamReader);
//
//                                            if(edottoService.isStrutturaEsclusa(String.valueOf(struttura.getCodiceEdotto()))) {
//                                                logger.info("Struttura esclusa dalla regione");
//                                            } else {
//                                                OrganigrammaStrutturaDto strutturaEsistente = edottoService.findOneByCodiceEdotto(struttura.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(struttura.getCodiceEdotto()) : null;
//                                                if (strutturaEsistente == null) {
//                                                    struttura.setTipologia(dizionarioService.getDizionario(90008));
//                                                    edottoService.identificaAndElaboraStruttura(struttura, streamReader, Optional.of(file));
//                                                    edottoService.salvaStruttura(struttura);
//                                                } else {
//                                                    edottoMapper.aggiornaStrutturaDto(struttura, strutturaEsistente);
//                                                    edottoService.identificaAndElaboraStruttura(strutturaEsistente, streamReader, Optional.of(file));
//                                                    edottoService.salvaStruttura(strutturaEsistente);
//                                                }
//                                            }
//                                        }
//                                    }
//                                } while (streamReader.hasNext());
//
//                                fileElaborati.add(file.getFileName().toString());
//                                streamReader.close();
//                                logger.info(String.format("Fine Allineamento %s", nomeFile));
//
//                            } catch (IOException e) {
//                                try {
//                                    throw new IOException(e.getMessage());
//                                } catch (IOException ex) {
//                                    throw new RuntimeException(ex);
//                                }
//                            } catch (XMLStreamException e) {
//                                throw new RuntimeException(e);
//                            }
//                                }
//                            });
//                } catch (IOException e) {
//                logger.error(e.getMessage());
//                throw new IOException(e.getMessage());
//            }
//            logger.info("Fine Allineamento Strutture");

            // ELABORO I FILE RESTANTI
//            try(Stream<Path> stream = Files.list(path)) {
//                for(Path file : stream.toList()) {
//                    String nomeFile = file.getFileName().toString();
//
//                    if (!(fileElaborati.contains(nomeFile)) && file.endsWith(".xml")) {
//                        logger.info(String.format("Allineamento %s", edottoService.getNomeFile(nomeFile)));
//
//                        try (BufferedReader reader = new BufferedReader(new FileReader(file.toFile()))) {
//                            XMLInputFactory inputFactory = XMLInputFactory.newInstance();
//                            XMLStreamReader streamReader = inputFactory.createXMLStreamReader(reader);
//
//                            int evento;
//                            String tag = "";
//
//                            do {
//                                evento = streamReader.next();
//
//                                if (evento == XMLStreamReader.START_ELEMENT) {
//                                    tag = streamReader.getLocalName().trim();
//
//                                    if (tag.equalsIgnoreCase("datiStrutturaSanitaria")) {
//                                        OrganigrammaStrutturaDto struttura = new OrganigrammaStrutturaDto();
//                                        edottoService.setDatiGeneraliStruttura(struttura, streamReader);
//
//                                        if(edottoService.isStrutturaEsclusa(String.valueOf(struttura.getCodiceEdotto()))) {
//                                            logger.info("Struttura esclusa dalla regione");
//                                        } else {
//                                            OrganigrammaStrutturaDto strutturaEsistente = edottoService.findOneByCodiceEdotto(struttura.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdotto(struttura.getCodiceEdotto()) : null;
//                                            if (strutturaEsistente == null) {
//                                                struttura.setTipologia(dizionarioService.getDizionario(90008));
//                                                edottoService.identificaAndElaboraStruttura(struttura, streamReader, xmlStabOspedaliero);
//                                                edottoService.salvaStruttura(struttura);
//                                            } else {
//                                                edottoMapper.aggiornaStrutturaDto(struttura, strutturaEsistente);
//                                                edottoService.identificaAndElaboraStruttura(strutturaEsistente, streamReader, xmlStabOspedaliero);
//                                                edottoService.salvaStruttura(strutturaEsistente);
//                                            }
//                                        }
//                                    }
//                                }
//                            } while (streamReader.hasNext());
//
//                            fileElaborati.add(file.getFileName().toString());
//                            streamReader.close();
//                            logger.info(String.format("Fine Allineamento %s", nomeFile));
//
//                        } catch (XMLStreamException e) {
//                            logger.error(String.format("Errore 'XMLStream' durante l'elaborazione di: %s \n%s", file.getFileName().toString(), e.getMessage()));
//                            throw new XMLStreamException(e.getMessage());
//                        } catch (Exception ex) {
//                            logger.error(String.format("Errore durante l'elaborazione di: %s \n%s", file.getFileName().toString(), ex.getMessage()));
//                            throw new Exception(ex.getMessage());
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                logger.error(e.getMessage());
//                throw new IOException(e.getMessage());
//            }

            try {
                FileInputStream file = new FileInputStream(new File(path + "/PresidiPrivatidiLabAnalisi.xls"));
                Workbook workbook = new XSSFWorkbook(file);
                Sheet sheet = workbook.getSheetAt(0);

                logger.info("Allineamento Laboratori");
                logger.info("Allineamento Laboratori - Presidi Privati");

                int i = 0;
                for(Row row : sheet) {
                    try {
                        if (row.getRowNum() == 0) {
                            continue;
                        }

                        Cell codice = row.getCell(2);
                        if (codice == null || codice.getCellType() == CellType.BLANK) {
                            break;
                        }

                        String stringCodice = codice.getStringCellValue().trim();
                        int codiceEdotto = Integer.parseInt(stringCodice);

                        OrganigrammaStrutturaDto struttura = edottoService.findOneByCodiceEdotto(codiceEdotto) != null ? edottoService.findOneByCodiceEdotto(codiceEdotto) : null;
                        if (struttura != null) {
                            logger.info("Struttura trovata");
                            OrganigrammaStrutturaDto figlia = edottoService.findOneByCodiceEdottoOfOriginal(codiceEdotto) != null ? edottoService.findOneByCodiceEdottoOfOriginal(codiceEdotto) : null;
                            if (figlia == null) {
                                logger.info("Struttura figlia non trovata");
                                figlia = new OrganigrammaStrutturaDto();
                                edottoMapper.aggiornaStrutturaDto(figlia, struttura);
                            } else {
                                logger.info("Struttura figlia trovata");

                            }
                            figlia.setTipologiaEdotto(dizionarioService.getDizionario(24022));
                            figlia.setStrutturaOriginale(struttura);
                            figlia.setParent(struttura);

                            edottoService.salvaStruttura(figlia);
                        } else {
                            logger.warn("Struttura non trovata");
                        }
                    } catch (Exception ex) {
                        throw new Exception(ex.getMessage());
                    }
                }
                logger.info("Fine Allineamento Laboratori - Presidi Privati");
                workbook.close();
                file.close();

            } catch (IOException e) {
                throw new Exception(e.getMessage());
            }

            logger.info("Allineamento Laboratori - Servizi ospedalieri LA");
            String[] codici = {"LA","PC","AP","CT","L","IT","MB","MC"};
            try {
                List<OrganigrammaStruttura> labs = edottoService.findByCodiciSpecialitaClinica(codici);
                for(OrganigrammaStruttura lab : labs) {
                    logger.debug(String.format("Codice edotto: %s", lab.getCodiceEdotto()));
                    OrganigrammaStrutturaDto figlia = edottoService.findOneByCodiceEdottoOfOriginal(lab.getCodiceEdotto()) != null ? edottoService.findOneByCodiceEdottoOfOriginal(lab.getCodiceEdotto()) : null;
                    if(figlia == null) {
                        logger.debug("Struttura Figlia non trovata");
                        //clone lab?!
                        figlia.setId(null);
                    } else {
                        logger.debug(String.format("Struttura Figlia trovata. Id: %s", figlia.getId()));
                        // updateChildRecord ?!?!
                    }
                    figlia.setTipologiaEdotto(dizionarioService.getDizionario(24022));
                    figlia.setStrutturaOriginale(edottoMapper.orgStrutturaEntityToDto(lab));
                    figlia.setParent(edottoMapper.orgStrutturaEntityToDto(lab));
                    figlia.setCodiceSpecialitaClinica(null);

                    edottoService.salvaStruttura(figlia);
                    logger.info("Fine Allineamento Laboratori - Servizi ospedalieri LA");
                    logger.info("Fine Allineamento Laboratori");
                }
            } catch (Exception e) {
                throw new Exception(e.getMessage());
            }
        } else {
            logger.error("Il percorso fornito non esiste.");
        }

        logger.debug("FINE Allineamento Strutture EDOTTO");
        return ResponseEntity.status(200).body("Allineamento Strutture Edotto terminato con successo.");
    }
}
