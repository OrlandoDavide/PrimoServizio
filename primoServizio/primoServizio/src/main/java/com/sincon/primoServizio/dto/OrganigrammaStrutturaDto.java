package com.sincon.primoServizio.dto;

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
    private OrganigrammaStrutturaDto asl;
    private String nomeTitolare;
    private String cognomeTitolare;
    private String emailTitolare;
    private String telefonoTitolare;
    private String nomeReferente;
    private String cognomeReferente;
    private String cfReferente;
    private DizionarioDto tipologia;
    private OrganigrammaStrutturaDto parent;
    private List<OrganigrammaStrutturaDto> childrens;
    private OrganigrammaStrutturaDto strutturaOriginale;
    private boolean abilitato;
    private int pubblico;
    private boolean isAsl;
    private boolean reteRegionale;
    private DizionarioDto categoria;
    private String codiceTs;
    private String codCap;
    private String codMinisteriale;
    private String codProvinciale;
    private String codiceNSIS;
    private String codiceSts11;
    private String codiceHsp11;
    private String codiceHsp12;
    private int codiceEdotto;
    private ComuneDto comune;
    private LocalDate dataApertura;
    private LocalDate dataChiusura;
    private LocalDate dataAttivazione;
    private DizionarioDto tipologiaGiuridica;
    private DizionarioDto tipologiaEdotto;
    private OrganigrammaStrutturaDto distretto;
    private int progDistretto;
    private int progServizio;
    private int progStabilimento;
    private int progReparto;
    private int progCO;
    private Long createdBy;
    private int createdWith;
    private LocalDate createdDate;
    private Long modifiedBy;
    private int modifiedWith;
    private LocalDate modifiedDate;
    private Long deletedBy;
    private int deletedWith;
    private LocalDate deletedDate;
    private String codiceSpecialitaClinica;
    private LocalDate dataAllineamento;
    private boolean screening;
    private boolean puntoPrelievo;
}
