package com.sincon.primoServizio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DizionarioDto {

    private Long id;
    private String denominazione;
    private String categoria;
    private String codifica;
    private int malattia;
    private int parent;
    private int ordine;
}
