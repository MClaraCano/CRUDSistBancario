package com.webclara.pruebaspring.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class TransferDto {

    private Long id;

    private Long origin;

    private Long target;

    private Date date;

    private BigDecimal amount;
}
