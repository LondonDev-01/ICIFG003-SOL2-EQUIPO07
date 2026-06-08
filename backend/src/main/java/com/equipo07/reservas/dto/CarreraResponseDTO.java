package com.equipo07.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarreraResponseDTO {
    private Integer id;
    private String nombreCarrera;
    private String facultad;
}