package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

@Entity
@QueryEntity
@Table(name = "DIZIONARIO", schema = "STRUTTURE")
@Data
@Component
public class Dizionario {

    @Id
    @Column(name = "ID_DIZIONARIO")
    private int id;

    @Column(name = "DENOMINAZIONE")
    private String denominazione;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Column(name = "CODIFICA", length = 16)
    private String codifica;

    @Column(name = "CUSTOM_FLAG", length = 8)
    private String customFlag;

    @Column(name = "ATTIVO")
    private boolean attivo;

    @Column(name = "MALATTIA")
    private int malattia;

    @Column(name = "PARENT")
    private int parent;

    @Column(name = "ORDINE")
    private int ordine;
}
