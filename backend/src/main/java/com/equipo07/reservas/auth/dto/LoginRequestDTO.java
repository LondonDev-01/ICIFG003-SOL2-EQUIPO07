package com.equipo07.reservas.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

    @NotBlank(message = "El RUT es obligatorio")
    private String rut;

    @NotBlank(message = "La contraseña es obligatoria")
    @Pattern(regexp = "\\d{4}", message = "La contraseña debe ser exactamente 4 dígitos numéricos")
    private String password;
}