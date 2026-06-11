package com.equipo07.reservas.service;

import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.repository.EstadoReservaRepository;
import com.equipo07.reservas.mapper.EstadoReservaMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EstadoReservaServiceImplTest {

    @Mock
    private EstadoReservaRepository estadoReservaRepository;

    @Mock
    private EstadoReservaMapper estadoReservaMapper;

    @InjectMocks
    private EstadoReservaServiceImpl estadoReservaService;

    @Test
    void obtenerPorId_conIdInexistente_lanzaResourceNotFoundException() {
        // Given: el repositorio no encuentra el estado
        when(estadoReservaRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then: el servicio lanza la excepción esperada
        assertThatThrownBy(() -> estadoReservaService.obtenerPorId(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void obtenerPorId_conIdExistente_retornaEstadoMapeado() {
        // Given: el repositorio encuentra un estado
        com.equipo07.reservas.entity.EstadoReserva estado = new com.equipo07.reservas.entity.EstadoReserva();
        estado.setIdEstado(1);
        estado.setNombreEstado("Confirmada");

        com.equipo07.reservas.dto.EstadoReservaResponseDTO responseDTO =
                new com.equipo07.reservas.dto.EstadoReservaResponseDTO(1, "Confirmada");

        when(estadoReservaRepository.findById(1)).thenReturn(Optional.of(estado));
        when(estadoReservaMapper.toResponse(estado)).thenReturn(responseDTO);

        // When: consultamos por id existente
        com.equipo07.reservas.dto.EstadoReservaResponseDTO resultado = estadoReservaService.obtenerPorId(1);

        // Then: retorna el DTO mapeado con los datos correctos
        assertThat(resultado).isNotNull();
        assertThat(resultado.getIdEstado()).isEqualTo(1);
        assertThat(resultado.getNombreEstado()).isEqualTo("Confirmada");
    }
}