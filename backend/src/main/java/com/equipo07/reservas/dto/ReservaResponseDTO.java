package com.equipo07.reservas.dto;

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
public class ReservaResponseDTO {
    private Integer id;
    private LocalDate fechaReserva;
    private String observacion;
    private LocalDateTime fechaCreacion;

    private Integer idEstudiante;
    private String nombreEstudiante;

    private Integer idSala;
    private String nombreSala;

    private Integer idHorario;
    private String bloqueHorario;

    private Integer idEstado;
    private String nombreEstado;
}