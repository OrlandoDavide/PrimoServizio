package com.sincon.primoServizio.dto;

import com.sincon.primoServizio.model.OrganigrammaStruttura;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class OrganigrammaStrutturaDto {
    private Long id;
    private String denominazione;
    private String codice;
    private int asl;
    private String nomeTitolare;
    private String cognomeTitolare;
    private String emailTitolare;
    private String telefonoTitolare;
    private String nomeReferente;
    private String cognomeReferente;
    private String cfReferente;
    private int tipologia;
    private int parent;
    private List<OrganigrammaStruttura> childrens;
    private int strutturaOriginale;
    private boolean abilitato;
    private int pubblico;
    private boolean isAsl;
    private boolean reteRegionale;
    private int categoria;
    private String codiceTs;
    private String codCap;
    private String codMinisteriale;
    private String codProvinciale;
    private String codiceNSIS;
    private String codiceSts11;
    private String codiceHsp11;
    private String codiceHsp12;
    private int codiceEdotto;
    private String comune;
    private LocalDate dataApertura;
    private LocalDate dataChiusura;
    private LocalDate dataAttivazione;
    private int tipologiaGiuridica;
    private int tipologiaEdotto;
    private int distretto;
    private int progDistretto;
    private int progServizio;
    private int progStabilimento;
    private int progReparto;
    private int progCO;
    private int createdBy;
    private int createdWith;
    private LocalDate createdDate;
    private int modifiedBy;
    private int modifiedWith;
    private LocalDate modifiedDate;
    private int deletedBy;
    private int deletedWith;
    private LocalDate deletedDate;
    private String codiceSpecialitaClinica;
    private LocalDate dataAllineamento;
    private boolean screening;
    private boolean puntoPrelievo;
}
