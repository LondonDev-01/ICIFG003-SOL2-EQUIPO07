package com.equipo07.reservas.auth.service;

public interface AuthService extends RateLimitTracker {
    com.equipo07.reservas.auth.dto.AuthResponseDTO register(com.equipo07.reservas.auth.dto.RegisterRequestDTO request);
    com.equipo07.reservas.auth.dto.AuthResponseDTO login(com.equipo07.reservas.auth.dto.LoginRequestDTO request, String ip);
}