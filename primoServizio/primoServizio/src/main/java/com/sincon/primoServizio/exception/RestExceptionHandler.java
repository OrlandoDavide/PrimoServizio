package com.sincon.primoServizio.exception;

import com.sincon.primoServizio.model.ErrorResponse;
import com.sincon.primoServizio.service.UtenteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;

@ControllerAdvice
@RestController
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(RestExceptionHandler.class);

    // NOT FOUND - EXCEPTION
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> NotFoundHandlerException(NotFoundException ex) {
        ErrorResponse errore = new ErrorResponse();
        errore.setStatusCode(ex.getStatusCode());
        errore.setMessage(ex.getMessage());

        return new ResponseEntity<>(errore, HttpStatus.NOT_FOUND);
    }

    // SQL - EXCEPTION
    @ExceptionHandler(SQLException.class)
    public final ResponseEntity<ErrorResponse> SQLHandlerException(SQLException ex) {
        ErrorResponse errore = new ErrorResponse();
        errore.setStatusCode(ex.getErrorCode());
        errore.setMessage(ex.getMessage());

        return new ResponseEntity<>(errore, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
