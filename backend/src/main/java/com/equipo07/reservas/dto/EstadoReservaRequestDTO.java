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
public class EstadoReservaRequestDTO {

    @NotBlank(message = "El nombre del estado es obligatorio")
    @Size(max = 30)
    private String nombreEstado;
}