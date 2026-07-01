package com.equipo07.reservas.auth.controller;

import com.equipo07.reservas.auth.dto.AuthResponseDTO;
import com.equipo07.reservas.auth.dto.LoginRequestDTO;
import com.equipo07.reservas.auth.dto.RegisterRequestDTO;
import com.equipo07.reservas.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO request) {
        log.info("Iniciando proceso de registro para un nuevo usuario");
        
        AuthResponseDTO response = authService.register(request);
        
        log.info("Usuario registrado exitosamente");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@Valid @RequestBody LoginRequestDTO request, HttpServletRequest httpRequest) {
        String ip = httpRequest.getRemoteAddr();
        log.info("Intento de inicio de sesión recibido desde la IP: {}", ip);
        
        AuthResponseDTO response = authService.login(request, ip);
        
        log.info("Inicio de sesión exitoso procesado para la IP: {}", ip);
        return ResponseEntity.ok(response);
    }
}