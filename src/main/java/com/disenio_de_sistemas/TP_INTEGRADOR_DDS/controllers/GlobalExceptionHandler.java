package com.disenio_de_sistemas.TP_INTEGRADOR_DDS.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleDeserializationError(HttpMessageNotReadableException ex) {
        if (ex.getCause() instanceof NumberFormatException) {
            return new ResponseEntity<>("Ingrese valores numéricos válidos (números positivos).", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("Error en el formato de los campos numéricos. Procure ingresar números enteros positivos.", HttpStatus.BAD_REQUEST);
    }
}
