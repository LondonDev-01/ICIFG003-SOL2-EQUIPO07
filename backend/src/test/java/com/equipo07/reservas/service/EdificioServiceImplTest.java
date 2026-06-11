package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.EdificioResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import com.equipo07.reservas.exception.ResourceNotFoundException;
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
class EdificioServiceImplTest {

    @Mock
    private com.equipo07.reservas.repository.EdificioRepository edificioRepository;

    @Mock
    private com.equipo07.reservas.mapper.EdificioMapper edificioMapper;

    @InjectMocks
    private EdificioServiceImpl edificioService;

    @Test
    void obtenerPorId_conIdInexistente_lanzaResourceNotFoundException() {
        // Given: el repositorio no encuentra el edificio
        when(edificioRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then: el servicio lanza la excepción esperada
        assertThatThrownBy(() -> edificioService.obtenerPorId(999))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");
    }

    @Test
    void obtenerPorId_conIdExistente_retornaEdificioMapeado() {
        // Given: el repositorio encuentra un edificio
        Edificio edificio = new Edificio();
        edificio.setId(1);
        edificio.setNombreEdificio("Edificio A");
        edificio.setDireccion("Calle 123");

        EdificioResponseDTO responseDTO = new EdificioResponseDTO(1, "Edificio A", "Calle 123");

        when(edificioRepository.findById(1)).thenReturn(Optional.of(edificio));
        when(edificioMapper.toResponse(edificio)).thenReturn(responseDTO);

        // When: consultamos por id existente
        EdificioResponseDTO resultado = edificioService.obtenerPorId(1);

        // Then: retorna el DTO mapeado con los datos correctos
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombreEdificio()).isEqualTo("Edificio A");
        assertThat(resultado.getDireccion()).isEqualTo("Calle 123");
    }
}