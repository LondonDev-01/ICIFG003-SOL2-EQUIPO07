package com.equipo07.reservas.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservaRequestDTO {

    @NotNull(message = "La fecha de reserva es obligatoria")
    private LocalDate fechaReserva;

    @NotBlank(message = "La observación es obligatoria")
    @Size(max = 255)
    private String observacion;

    @NotNull(message = "La fecha de creación es obligatoria")
    @PastOrPresent
    private LocalDateTime fechaCreacion;

    @NotNull(message = "El id del estudiante es obligatorio")
    private Integer idEstudiante;

    @NotNull(message = "El id de la sala es obligatorio")
    private Integer idSala;

    @NotNull(message = "El id del horario es obligatorio")
    private Integer idHorario;

    @NotNull(message = "El id del estado es obligatorio")
    private Integer idEstado;
}