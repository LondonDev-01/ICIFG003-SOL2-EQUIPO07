package com.equipo07.reservas.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteRequestDTO {

    @Size(max = 12)
    private String rut;

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    private String nombre;

    @NotBlank(message = "El apellido es obligatorio")
    @Size(max = 100)
    private String apellido;

    @Email(message = "El correo debe tener un formato válido")
    @Size(max = 150)
    private String correo;

    @Size(max = 20)
    private String telefono;

    @NotNull(message = "La fecha de registro es obligatoria")
    @PastOrPresent
    private LocalDate fechaRegistro;

    @NotNull(message = "El id de la carrera es obligatorio")
    private Integer idCarrera;
}