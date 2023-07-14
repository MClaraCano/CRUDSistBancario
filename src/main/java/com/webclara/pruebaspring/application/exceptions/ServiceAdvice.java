package com.webclara.pruebaspring.application.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceAdvice {

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<NoSeEncuentraID> runtimeExcep (Exception e){
        NoSeEncuentraID errorService = NoSeEncuentraID.builder()
                .code("P-500")
                .mensaje("No se encontró el ID en la BBDD")
                .build();
        return new ResponseEntity<>(errorService, HttpStatus.BAD_REQUEST);
    }
}
