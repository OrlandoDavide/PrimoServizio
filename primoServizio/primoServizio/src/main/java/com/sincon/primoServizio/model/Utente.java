package com.sincon.primoServizio.model;

import com.querydsl.core.annotations.QueryEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Entity
@QueryEntity
@Table(name = "UTENTE", schema = "STRUTTURE")
@Data
public class Utente implements Serializable {

    @Id
    @Column(name = "ID_UTENTE")
    private Long id;

    @Column(name = "EMAIL", nullable = false, unique = true)
    private String email;

    @Column(name = "PASSWORD", nullable = false)
    private String password;

    @Column(name = "ATTIVO", columnDefinition = "boolean default true")
    private boolean attivo = true;
}
