package com.equipo07.reservas.security;

import com.equipo07.reservas.entity.Estudiante;

/**
 * Custom principal que envuelve al Estudiante autenticado.
 * Permite acceder a id, rut, nombre directamente desde @AuthenticationPrincipal.
 */
public record EstudiantePrincipal(Estudiante estudiante) {}