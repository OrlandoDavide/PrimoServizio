package com.sincon.primoServizio.exception;

import com.sincon.primoServizio.model.ErrorResponse;
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

    // NOT FOUND - EXCEPTION
    @ExceptionHandler(NotFoundException.class)
    public final ResponseEntity<ErrorResponse> NotFoundHandlerException(Exception ex) {
        ErrorResponse errore = new ErrorResponse();
        errore.setStatusCode(404);

        return null;
    }

    // SQL - EXCEPTION
    @ExceptionHandler(SQLException.class)
    public final ResponseEntity<String> SQLHandlerException(SQLException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore SQL => " + ex.getMessage());
    }
}
