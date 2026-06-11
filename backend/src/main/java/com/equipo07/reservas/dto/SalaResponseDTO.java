package com.equipo07.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaResponseDTO {

    private Integer id;
    private String codigoSala;
    private String nombreSala;
    private Integer capacidad;
    private Integer piso;
    private String descripcion;
    private String estado;

}