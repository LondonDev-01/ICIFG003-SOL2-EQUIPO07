package com.equipo07.reservas.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
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

    @NotNull(message = "El id de la carrera es obligatorio")
    private Integer idCarrera;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "\\d{4}", message = "La contraseña debe ser exactamente 4 dígitos numéricos")
    private String password;
}