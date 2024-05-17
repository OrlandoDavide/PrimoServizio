package com.sincon.primoServizio.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends RuntimeException{

    private int statusCode = 404;
    private String message = "Risorsa non trovata";
}
