package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@QueryEntity
@Table(name = "DIZIONARIO", schema = "STRUTTURE")
@Data
public class Dizionario {

    @Id
    @Column(name = "ID_DIZIONARIO")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "DENOMINAZIONE")
    private String denominazione;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Column(name = "CODIFICA")
    private String codifica;

    @Column(name = "MALATTIA")
    private int malattia;

    @Column(name = "PARENT")
    private int parent;

    @Column(name = "ORDINE")
    private int ordine;
}
