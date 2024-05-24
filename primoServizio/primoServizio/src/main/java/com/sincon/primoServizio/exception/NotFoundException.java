package com.sincon.primoServizio.exception;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends RuntimeException{

    private int statusCode = 404;
    private String message = "Risorsa non trovata";
}
