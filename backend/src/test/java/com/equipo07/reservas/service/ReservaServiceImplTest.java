package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.entity.EstadoReserva;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Reserva;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.mapper.ReservaMapper;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.repository.EstadoReservaRepository;
import com.equipo07.reservas.repository.HorarioDisponibleRepository;
import com.equipo07.reservas.repository.ReservaRepository;
import com.equipo07.reservas.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservaServiceImplTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private HorarioDisponibleRepository horarioDisponibleRepository;

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @Mock
    private ReservaMapper reservaMapper;

    @InjectMocks
    private ReservaServiceImpl reservaService;

    @Test
    void crear_conIdEstudianteInexistente_lanzaResourceNotFoundException() {
        // Given: el DTO referencia un estudiante que no existe
        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setIdEstudiante(999);
        request.setIdSala(1);
        request.setIdHorario(1);
        request.setIdEstado(1);
        request.setFechaReserva(LocalDate.now());
        request.setObservacion("Test");
        request.setFechaCreacion(LocalDateTime.now());

        when(estudianteRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then: el servicio lanza 404 indicando el id del estudiante
        assertThatThrownBy(() -> reservaService.crear(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void crear_conTodasLasFKsValidas_guardaReservaYRetornaDTO() {
        // Given: todas las FKs existen
        LocalDateTime fechaCreacion = LocalDateTime.now();

        ReservaRequestDTO request = new ReservaRequestDTO();
        request.setIdEstudiante(1);
        request.setIdSala(1);
        request.setIdHorario(1);
        request.setIdEstado(1);
        request.setFechaReserva(LocalDate.now());
        request.setObservacion("Reserva de estudio");
        request.setFechaCreacion(fechaCreacion);

        Estudiante estudiante = new Estudiante();
        estudiante.setId(1);
        estudiante.setNombre("Juan");
        estudiante.setApellido("Pérez");

        Sala sala = new Sala();
        sala.setId(1);
        sala.setNombreSala("Sala 101");

        HorarioDisponible horario = new HorarioDisponible();
        horario.setId(1);
        horario.setHoraInicio(java.time.LocalTime.of(8, 0));
        horario.setHoraTermino(java.time.LocalTime.of(9, 30));

        EstadoReserva estado = new EstadoReserva();
        estado.setIdEstado(1);
        estado.setNombreEstado("Confirmada");

        Reserva reservaPersistida = new Reserva();
        reservaPersistida.setId(100);
        reservaPersistida.setFechaReserva(request.getFechaReserva());
        reservaPersistida.setObservacion("Reserva de estudio");
        reservaPersistida.setFechaCreacion(fechaCreacion);
        reservaPersistida.setEstudiante(estudiante);
        reservaPersistida.setSala(sala);
        reservaPersistida.setHorario(horario);
        reservaPersistida.setEstado(estado);

        ReservaResponseDTO responseEsperado = new ReservaResponseDTO();
        responseEsperado.setId(100);
        responseEsperado.setObservacion("Reserva de estudio");
        responseEsperado.setIdEstudiante(1);
        responseEsperado.setNombreEstudiante("Juan");
        responseEsperado.setIdSala(1);
        responseEsperado.setNombreSala("Sala 101");
        responseEsperado.setIdEstado(1);
        responseEsperado.setNombreEstado("Confirmada");

        when(estudianteRepository.findById(1)).thenReturn(Optional.of(estudiante));
        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
        when(horarioDisponibleRepository.findById(1)).thenReturn(Optional.of(horario));
        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estado));
        when(reservaMapper.toEntity(request)).thenReturn(reservaPersistida);
        when(reservaRepository.save(reservaPersistida)).thenReturn(reservaPersistida);
        when(reservaMapper.toResponse(reservaPersistida)).thenReturn(responseEsperado);

        // When: creamos la reserva
        ReservaResponseDTO resultado = reservaService.crear(request);

        // Then: retorna el DTO con id autogenerado y todas las FKs resueltas
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(100);
        assertThat(resultado.getNombreEstudiante()).isEqualTo("Juan");
        assertThat(resultado.getNombreSala()).isEqualTo("Sala 101");
        assertThat(resultado.getNombreEstado()).isEqualTo("Confirmada");
    }
}