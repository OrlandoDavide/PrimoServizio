package com.sincon.primoServizio.model;

import lombok.Data;

@Data
public class ErrorResponse {

    private int statusCode;
    private String message;
}
