package com.equipo07.reservas.auth.service.impl;

import com.equipo07.reservas.auth.dto.AuthResponseDTO;
import com.equipo07.reservas.auth.dto.LoginRequestDTO;
import com.equipo07.reservas.auth.dto.RegisterRequestDTO;
import com.equipo07.reservas.auth.exception.AuthException;
import com.equipo07.reservas.auth.service.AuthService;
import com.equipo07.reservas.entity.Carrera;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.repository.CarreraRepository;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.security.JwtService;
import com.equipo07.reservas.security.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RateLimitFilter rateLimitFilter;

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        String rutNormalizado = normalizeRut(request.getRut());

        if (estudianteRepository.findByRut(rutNormalizado).isPresent()
                || estudianteRepository.findByRut(request.getRut()).isPresent()) {
            throw new AuthException("Ya existe un estudiante registrado con el RUT: " + request.getRut());
        }
        if (estudianteRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new AuthException("Ya existe un estudiante registrado con el correo: " + request.getCorreo());
        }

        Carrera carrera = carreraRepository.findById(request.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + request.getIdCarrera()));

        Estudiante estudiante = new Estudiante();
        estudiante.setRut(request.getRut());
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());
        estudiante.setCorreo(request.getCorreo());
        estudiante.setTelefono(request.getTelefono());
        estudiante.setFechaRegistro(LocalDate.now());
        estudiante.setCarrera(carrera);
        estudiante.setPassword(passwordEncoder.encode(request.getPassword()));

        Estudiante saved = estudianteRepository.save(estudiante);
        return buildResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponseDTO login(LoginRequestDTO request, String ip) {
        String rutNormalizado = normalizeRut(request.getRut());

        Estudiante estudiante = estudianteRepository.findByRut(rutNormalizado)
                .or(() -> estudianteRepository.findByRut(request.getRut()))
                .orElse(null);

        if (estudiante == null || !passwordEncoder.matches(request.getPassword(), estudiante.getPassword())) {
            rateLimitFilter.registerFailedAttempt(ip);
            throw new AuthException("Credenciales inválidas");
        }

        rateLimitFilter.registerSuccessfulAttempt(ip);
        return buildResponse(estudiante);
    }

    @Override
    public void registerFailedAttempt(String ip) {
        rateLimitFilter.registerFailedAttempt(ip);
    }

    @Override
    public void registerSuccessfulAttempt(String ip) {
        rateLimitFilter.registerSuccessfulAttempt(ip);
    }

    private AuthResponseDTO buildResponse(Estudiante estudiante) {
        String token = jwtService.generate(estudiante.getRut());
        return new AuthResponseDTO(
                token,
                estudiante.getRut(),
                estudiante.getNombre() + " " + estudiante.getApellido(),
                jwtService.getExpirationSeconds()
        );
    }

    /**
     * Normaliza un RUT eliminando puntos, guiones y convirtiendo a mayúsculas.
     * Acepta formatos: 12.345.678-9, 12345678-9, 123456789
     */
    private String normalizeRut(String rut) {
        if (rut == null) return null;
        return rut.replaceAll("[.\\-]", "").toUpperCase();
    }
}