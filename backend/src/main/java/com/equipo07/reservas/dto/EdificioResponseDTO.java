package com.equipo07.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdificioResponseDTO {
    private Integer id;
    private String nombreEdificio;
    private String direccion;
}