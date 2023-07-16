package com.webclara.pruebaspring.application.exceptions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vacio {

    private String code;
    private String mensaje;

}
