package com.webclara.pruebaspring.application.exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceAdvice {

    @ExceptionHandler(value = ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<NoSeEncuentraID> noSeEncontroId (ChangeSetPersister.NotFoundException e){
        NoSeEncuentraID errorService = NoSeEncuentraID.builder()
                .code("P-500")
                .mensaje("No se encontró el ID en la BBDD")
                .build();
        return new ResponseEntity<>(errorService, HttpStatus.BAD_REQUEST);
    }



    @ExceptionHandler(value = NullPointerException.class)
    public ResponseEntity<Vacio> valoresVacios (NullPointerException e){
        Vacio campoVacio = Vacio.builder()
                .code("P-500")
                .mensaje("Falta definir ussername y/o password")
                .build();
        return new ResponseEntity<>(campoVacio, HttpStatus.BAD_REQUEST);
    }
}
