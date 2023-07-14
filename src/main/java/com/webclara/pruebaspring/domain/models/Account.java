package com.webclara.pruebaspring.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Data
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private BigDecimal balance;

    // TODO: Investigar que sucede con las relaciones bidireccionales y por qué motivo puede ocurrir
    //  un error de recursividad
    @ManyToOne
    private User owner;

}
