package com.equipo07.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdificioRequestDTO {

    @NotBlank(message = "El nombre del edificio es obligatorio")
    @Size(max = 100)
    private String nombreEdificio;

    @NotBlank(message = "La dirección es obligatoria")
    @Size(max = 200)
    private String direccion;
}