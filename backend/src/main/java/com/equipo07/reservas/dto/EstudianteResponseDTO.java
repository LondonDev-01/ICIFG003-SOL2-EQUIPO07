package com.equipo07.reservas.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteResponseDTO {
    private Integer id;
    private String rut;
    private String nombre;
    private String apellido;
    private String correo;
    private String telefono;
    private LocalDate fechaRegistro;
    private Integer idCarrera;
    private String nombreCarrera;
}