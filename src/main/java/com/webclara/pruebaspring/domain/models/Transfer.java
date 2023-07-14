package com.webclara.pruebaspring.domain.models;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transfer_id")
    private Long id;

    private Long origin;

    private Long target;

    private Date date;

    private BigDecimal amount;

}
