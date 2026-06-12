package com.equipo07.reservas.auth;

import com.equipo07.reservas.auth.dto.AuthResponseDTO;
import com.equipo07.reservas.auth.dto.LoginRequestDTO;
import com.equipo07.reservas.auth.dto.RegisterRequestDTO;
import com.equipo07.reservas.auth.exception.AuthException;
import com.equipo07.reservas.auth.service.impl.AuthServiceImpl;
import com.equipo07.reservas.entity.Carrera;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.repository.CarreraRepository;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.security.JwtService;
import com.equipo07.reservas.security.RateLimitFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private CarreraRepository carreraRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private RateLimitFilter rateLimitFilter;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_conDatosValidos_creaEstudianteYDevuelveToken() {
        // Given
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setRut("12345678-9");
        request.setNombre("Juan");
        request.setApellido("Pérez");
        request.setCorreo("juan@correo.cl");
        request.setIdCarrera(1);
        request.setPassword("1234");

        Carrera carrera = new Carrera();
        carrera.setId(1);
        carrera.setNombreCarrera("Ing. Informática");

        Estudiante saved = new Estudiante();
        saved.setId(10);
        saved.setRut("12345678-9");
        saved.setNombre("Juan");
        saved.setApellido("Pérez");
        saved.setPassword("$2a$10$hashed");
        saved.setCarrera(carrera);

        when(estudianteRepository.findByRut(any())).thenReturn(Optional.empty());
        when(estudianteRepository.findByCorreo("juan@correo.cl")).thenReturn(Optional.empty());
        when(carreraRepository.findById(1)).thenReturn(Optional.of(carrera));
        when(passwordEncoder.encode("1234")).thenReturn("$2a$10$hashed");
        when(estudianteRepository.save(any(Estudiante.class))).thenReturn(saved);
        when(jwtService.generate("12345678-9")).thenReturn("mocked.jwt.token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        AuthResponseDTO response = authService.register(request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
        assertThat(response.getRut()).isEqualTo("12345678-9");
        assertThat(response.getNombre()).isEqualTo("Juan Pérez");
        assertThat(response.getExpiresIn()).isEqualTo(3600L);
    }

    @Test
    void register_conRutDuplicado_lanzaAuthException() {
        // Given
        RegisterRequestDTO request = new RegisterRequestDTO();
        request.setRut("12345678-9");
        request.setCorreo("nuevo@correo.cl");
        request.setIdCarrera(1);
        request.setPassword("1234");
        request.setNombre("A");
        request.setApellido("B");

        Estudiante existente = new Estudiante();
        existente.setRut("12345678-9");

        when(estudianteRepository.findByRut(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(existente));

        // When/Then
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("RUT");
    }

    @Test
    void login_conCredencialesValidas_devuelveToken() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setRut("12345678-9");
        request.setPassword("1234");

        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        estudiante.setNombre("Juan");
        estudiante.setApellido("Pérez");
        estudiante.setPassword("$2a$10$hashed");

        when(estudianteRepository.findByRut(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(estudiante));
        when(passwordEncoder.matches("1234", "$2a$10$hashed")).thenReturn(true);
        when(jwtService.generate("12345678-9")).thenReturn("mocked.jwt.token");
        when(jwtService.getExpirationSeconds()).thenReturn(3600L);

        // When
        AuthResponseDTO response = authService.login(request, "127.0.0.1");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked.jwt.token");
    }

    @Test
    void login_conPasswordIncorrecto_lanzaAuthException() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setRut("12345678-9");
        request.setPassword("9999");

        Estudiante estudiante = new Estudiante();
        estudiante.setRut("12345678-9");
        estudiante.setPassword("$2a$10$hashed");

        when(estudianteRepository.findByRut(org.mockito.ArgumentMatchers.anyString()))
                .thenReturn(Optional.of(estudiante));
        when(passwordEncoder.matches("9999", "$2a$10$hashed")).thenReturn(false);

        // When/Then
        assertThatThrownBy(() -> authService.login(request, "127.0.0.1"))
                .isInstanceOf(AuthException.class)
                .hasMessageContaining("Credenciales inválidas");
    }

    @Test
    void login_conRutInexistente_lanzaAuthException() {
        // Given
        LoginRequestDTO request = new LoginRequestDTO();
        request.setRut("99999999-9");
        request.setPassword("1234");

        when(estudianteRepository.findByRut(any())).thenReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> authService.login(request, "127.0.0.1"))
                .isInstanceOf(AuthException.class);
    }
}