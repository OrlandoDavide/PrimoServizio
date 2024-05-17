package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@QueryEntity
@Table(name = "UTENTE", schema = "STRUTTURE")
@Data
public class Utente {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EMAIL", nullable = false)
    private String email;

    @Column(name = "PASSWORD", unique = true, nullable = false)
    private String password;

    @Column(name = "ATTIVO", columnDefinition = "boolean default true") //non funziona per tutti i DB
    private boolean attivo = true;
}
