package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@QueryEntity
@Table(name = "comune", schema = "strutture")
@Data
public class Comune {

    @Id
    @Column(name = "ISTAT")
    private String istat;

    @Column(name = "DENOMINAZIONE", length = 40)
    private String nome;

    @Column(name = "SIGLAPROV", length = 40)
    private String provincia;

    @Column(name = "ASL", length = 40)
    private String asl;

    @Column(name = "ATTIVO", length = 1)
    private String attivo;
}
