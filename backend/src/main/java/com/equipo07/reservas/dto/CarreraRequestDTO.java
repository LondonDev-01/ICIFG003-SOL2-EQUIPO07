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
public class CarreraRequestDTO {

    @NotBlank(message = "El nombre de la carrera es obligatorio")
    @Size(max = 100)
    private String nombreCarrera;

    @NotBlank(message = "La facultad es obligatoria")
    @Size(max = 100)
    private String facultad;
}