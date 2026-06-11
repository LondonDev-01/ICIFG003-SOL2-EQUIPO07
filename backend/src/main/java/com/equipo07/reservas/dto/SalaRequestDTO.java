package com.equipo07.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SalaRequestDTO {

    @Size(max = 20)
    private String codigoSala;

    @NotBlank(message = "El nombre de la sala es obligatorio")
    @Size(max = 100)
    private String nombreSala;

    @NotNull(message = "La capacidad es obligatoria")
    @Positive(message = "La capacidad debe ser positiva")
    private Integer capacidad;

    @NotNull(message = "El piso es obligatorio")
    @Positive(message = "El piso debe ser positivo")
    private Integer piso;

    @NotBlank(message = "La descripción es obligatoria")
    @Size(max = 255)
    private String descripcion;

    @NotBlank(message = "El estado es obligatorio")
    @Size(max = 30)
    private String estado;

    @NotNull(message = "El id del edificio es obligatorio")
    private Integer idEdificio;
}