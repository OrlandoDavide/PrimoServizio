package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.context.annotation.Primary;

import java.io.Serializable;

@Entity
@Table(name = "UTENTE", schema = "STRUTTURE")
@Data
@QueryEntity
public class Utente implements Serializable {

    @Id
    @SequenceGenerator(name = "UTENTE_SEQ", sequenceName = "UTENTE_SEQ", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UTENTE_SEQ")
    @Column(name = "ID_UTENTE")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ATTIVO")
    private boolean attivo = true;
}
