package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@QueryEntity
@Table(name = "comune", schema = "strutture")
@Data
public class Comune {

    @Id
    @Column(name = "ISTAT")
    private String istat;

    @Column(name = "DENOMINAZIONE")
    private String denominazione;

    @Column(name = "SIGLAPROV")
    private String provincia;

    @Column(name = "ASL")
    private String asl;

    @Column(name = "ATTIVO")
    private String attivo;
}
