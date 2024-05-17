package com.sincon.primoServizio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtenteDto {

    private long id;
    private String email;
    private String password;
    private boolean attivo;
}
