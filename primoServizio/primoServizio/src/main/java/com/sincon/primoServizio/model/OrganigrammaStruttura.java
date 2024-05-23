package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Entity
@QueryEntity
@Table(name = "ORGANIGRAMMA_STRUTTURA", schema = "STRUTTURE")
@Data
@Component
public class OrganigrammaStruttura {

    @Id
    @Column(name = "ID_STRUTTURA")
    //@GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;

    @Column(name = "DENOMINAZIONE")
    private String denominazione;

    @Column(name = "CODICE")
    private String codice;

    @ManyToOne(targetEntity = OrganigrammaStruttura.class)
    @JoinColumn(name = "ID_ASL", referencedColumnName = "ID_STRUTTURA")
    private int asl;

    @Column(name = "NOME_TITOLARE")
    private String nomeTitolare;

    @Column(name = "COGNOME_TITOLARE")
    private String cognomeTitolare;

    @Column(name = "EMAIL_TITOLARE")
    private String emailTitolare;

    @Column(name = "TELEFONO_TITOLARE")
    private String telefonoTitolare;

    @Column(name = "NOME_REFERENTE")
    private String nomeReferente;

    @Column(name = "COGNOME_REFERENTE")
    private String cognomeReferente;

    @Column(name = "CF_REFERENTE")
    private String cfReferente;

    @ManyToOne(targetEntity = Dizionario.class)
    @JoinColumn(name = "ID_TIPOLOGIA", referencedColumnName = "ID_DIZIONARIO")
    private int tipologia;

    @ManyToOne(targetEntity = OrganigrammaStruttura.class)
    @JoinColumn(name = "ID_PARENT", referencedColumnName = "ID_STRUTTURA")
    private int parent;

    @OneToMany(targetEntity = OrganigrammaStruttura.class, mappedBy = "parent", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<OrganigrammaStruttura> childrens;

    @ManyToOne(targetEntity = OrganigrammaStruttura.class)
    @JoinColumn(name = "COD_STRUTTURA_ORIGINALE", referencedColumnName = "ID_STRUTTURA")
    private int strutturaOriginale;

    @Column(name = "ABILITATO")
    private boolean abilitato;

    @Column(name = "PUBBLICO")
    private int pubblico;

    @Column(name = "IS_ASL")
    private boolean isAsl;

    @Column(name = "RETE_REGIONALE")
    private boolean reteRegionale;

    @ManyToOne(targetEntity = Dizionario.class)
    @JoinColumn(name = "ID_CATEGORIA", referencedColumnName = "ID_DIZIONARIO")
    private int categoria;

    @Column(name = "CODICE_TS")
    private String coditeTs;

    @Column(name = "COD_CAP", length = 10)
    private String codCap;

    @Column(name = "COD_MINISTERIALE")
    private String codMinisteriale;

    @Column(name = "COD_PROVINCIALE")
    private String codProvinciale;

    @Column(name = "COD_NSIS")
    private String codiceNSIS;

    @Column(name = "CODICE_STS11")
    private String codiceSts11;

    @Column(name = "CODICE_HSP11")
    private String codiceHsp11;

    @Column(name = "CODICE_HSP12")
    private String codiceHsp12;

    @Column(name = "COD_EDOTTO", length = 11)
    private int codiceEdotto;

    @ManyToOne(targetEntity = Comune.class)
    @JoinColumn(name = "COD_ISTAT", referencedColumnName = "ISTAT")
    private String comune;

    @Column(name = "DATA_APERTURA")
    private LocalDate dataApertura;

    @Column(name = "DATA_CHIUSURA")
    private LocalDate dataChiusura;

    @Column(name = "DATA_ATTIVAZIONE")
    private LocalDate dataAttivazione;

    @ManyToOne(targetEntity = Dizionario.class)
    @JoinColumn(name = "ID_TIPOLOGIA_GIURIDICA", referencedColumnName = "ID_DIZIONARIO")
    private int tipologiaGiuridica;

    @ManyToOne(targetEntity = Dizionario.class)
    @JoinColumn(name = "ID_TIPOLOGIA_EDOTTO", referencedColumnName = "ID_DIZIONARIO")
    private int tipologiaEdotto;

    @ManyToOne(targetEntity = OrganigrammaStruttura.class)
    @JoinColumn(name = "ID_DISTRETTO", referencedColumnName = "ID_STRUTTURA")
    private int distretto;

    @Column(name = "PROG_DISTRETTO", length = 11)
    private int progDistretto;

    @Column(name = "PROG_SERVIZIO", length = 3)
    private int progServizio;

    @Column(name = "PROG_STABILIMENTO", length = 3)
    private int progStabilimento;

    @Column(name = "PROG_REPARTO", length = 3)
    private int progReparto;

    @Column(name = "PROG_CO", length = 3)
    private int progCO;

    @Column(name = "ID_CREATORE")
    private int createdBy;

    @Column(name = "ID_PROFILO_CREATORE")
    private int createdWith;

    @Column(name = "DATA_CREAZIONE")
    private LocalDate createdDate;

    @Column(name = "ID_ULTIMA_MODIFICA")
    private int modifiedBy;

    @Column(name = "ID_PROFILO_MODIFICA")
    private int modifiedWith;

    @Column(name = "DATA_MODIFICA")
    private LocalDate modifiedDate;

    @Column(name = "ID_DELETE")
    private int deletedBy;

    @Column(name = "ID_PROFILO_DELETE")
    private int deletedWith;

    @Column(name = "DATA_DELETE")
    private LocalDate deletedDate;

    @Column(name = "COD_SPEC_CLINICA")
    private String codiceSpecialitaClinica;

    @Column(name = "DATA_ALLINEAMENTO")
    private LocalDate dataAllineamento;

    @Column(name = "SCREENING")
    private boolean screening;

    @Column(name = "PUNTO_PRELIEVO")
    private boolean puntoPrelievo;
}
