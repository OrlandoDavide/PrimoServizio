package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name = "UTENTE", schema = "STRUTTURE")
@Data
@QueryEntity
public class Utente implements Serializable {

    @Id
    @Column(name = "ID_UTENTE")
    private Long id;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ATTIVO")
    private boolean attivo = true;
}
