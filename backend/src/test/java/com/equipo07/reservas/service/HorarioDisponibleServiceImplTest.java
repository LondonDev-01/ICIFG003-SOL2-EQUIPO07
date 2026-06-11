package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.HorarioDisponibleRequestDTO;
import com.equipo07.reservas.dto.HorarioDisponibleResponseDTO;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.mapper.HorarioDisponibleMapper;
import com.equipo07.reservas.repository.HorarioDisponibleRepository;
import com.equipo07.reservas.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HorarioDisponibleServiceImplTest {

    @Mock
    private HorarioDisponibleRepository horarioDisponibleRepository;

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private HorarioDisponibleMapper horarioDisponibleMapper;

    @InjectMocks
    private HorarioDisponibleServiceImpl horarioDisponibleService;

    @Test
    void crear_conIdSalaInexistente_lanzaResourceNotFoundException() {
        // Given: el DTO referencia una sala que no existe
        HorarioDisponibleRequestDTO request = new HorarioDisponibleRequestDTO();
        request.setIdSala(999);
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraTermino(LocalTime.of(9, 30));

        when(salaRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then: el servicio lanza 404 antes de intentar guardar el horario
        assertThatThrownBy(() -> horarioDisponibleService.crear(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void crear_conSalaValida_guardaHorarioYRetornaDTO() {
        // Given: la sala existe y el mapper convierte correctamente
        HorarioDisponibleRequestDTO request = new HorarioDisponibleRequestDTO();
        request.setIdSala(1);
        request.setHoraInicio(LocalTime.of(8, 0));
        request.setHoraTermino(LocalTime.of(9, 30));

        Sala sala = new Sala();
        sala.setId(1);
        sala.setNombreSala("Sala 101");

        HorarioDisponible horarioPersistido = new HorarioDisponible();
        horarioPersistido.setId(10);
        horarioPersistido.setHoraInicio(LocalTime.of(8, 0));
        horarioPersistido.setHoraTermino(LocalTime.of(9, 30));
        horarioPersistido.setSala(sala);

        HorarioDisponibleResponseDTO responseEsperado = new HorarioDisponibleResponseDTO(
                10, LocalTime.of(8, 0), LocalTime.of(9, 30), 1, "Sala 101");

        when(salaRepository.findById(1)).thenReturn(Optional.of(sala));
        when(horarioDisponibleMapper.toEntity(request)).thenReturn(horarioPersistido);
        when(horarioDisponibleRepository.save(horarioPersistido)).thenReturn(horarioPersistido);
        when(horarioDisponibleMapper.toResponse(horarioPersistido)).thenReturn(responseEsperado);

        // When: creamos el horario
        HorarioDisponibleResponseDTO resultado = horarioDisponibleService.crear(request);

        // Then: retorna el DTO con id autogenerado y datos correctos
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10);
        assertThat(resultado.getHoraInicio()).isEqualTo(LocalTime.of(8, 0));
        assertThat(resultado.getHoraTermino()).isEqualTo(LocalTime.of(9, 30));
        assertThat(resultado.getIdSala()).isEqualTo(1);
        assertThat(resultado.getNombreSala()).isEqualTo("Sala 101");
    }
}