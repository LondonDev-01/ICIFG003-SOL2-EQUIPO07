package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.SalaRequestDTO;
import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.repository.CarreraRepository;
import com.equipo07.reservas.repository.EdificioRepository;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.repository.SalaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalaServiceImplTest {

    @Mock
    private SalaRepository salaRepository;

    @Mock
    private EdificioRepository edificioRepository;

    @Mock
    private com.equipo07.reservas.mapper.SalaMapper salaMapper;

    @InjectMocks
    private SalaServiceImpl salaService;

    @Test
    void crear_conIdEdificioInexistente_lanzaResourceNotFoundException() {
        // Given: el DTO referencia un edificio que no existe
        SalaRequestDTO request = new SalaRequestDTO();
        request.setIdEdificio(999);
        request.setNombreSala("Sala 101");
        request.setCapacidad(20);
        request.setPiso(1);
        request.setDescripcion("Sala de estudio");
        request.setEstado("Disponible");
        request.setCodigoSala("A101");

        when(edificioRepository.findById(999)).thenReturn(Optional.empty());

        // When/Then: el servicio lanza 404 antes de intentar guardar la sala
        assertThatThrownBy(() -> salaService.crear(request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("999");

        // Y NUNCA debe intentar persistir la sala si el edificio no existe
        verify(salaRepository, never()).save(any(Sala.class));
    }

    @Test
    void crear_conEdificioValido_guardaSalaYRetornaDTO() {
        // Given: edificio existe y mapper convierte correctamente
        SalaRequestDTO request = new SalaRequestDTO();
        request.setIdEdificio(1);
        request.setCodigoSala("A101");
        request.setNombreSala("Sala 101");
        request.setCapacidad(20);
        request.setPiso(1);
        request.setDescripcion("Sala de estudio");
        request.setEstado("Disponible");

        Edificio edificio = new Edificio();
        edificio.setId(1);
        edificio.setNombreEdificio("Edificio A");
        edificio.setDireccion("Calle 123");

        Sala salaPersistida = new Sala();
        salaPersistida.setId(10);
        salaPersistida.setCodigoSala("A101");
        salaPersistida.setNombreSala("Sala 101");
        salaPersistida.setCapacidad(20);
        salaPersistida.setPiso(1);
        salaPersistida.setDescripcion("Sala de estudio");
        salaPersistida.setEstado("Disponible");
        salaPersistida.setEdificio(edificio);

        SalaResponseDTO responseEsperado = new SalaResponseDTO(
                10, "A101", "Sala 101", 20, 1,
                "Sala de estudio", "Disponible", 1, "Edificio A");

        when(edificioRepository.findById(1)).thenReturn(Optional.of(edificio));
        when(salaMapper.toEntity(request)).thenReturn(salaPersistida);
        when(salaRepository.save(salaPersistida)).thenReturn(salaPersistida);
        when(salaMapper.toResponse(salaPersistida)).thenReturn(responseEsperado);

        // When: creamos la sala
        SalaResponseDTO resultado = salaService.crear(request);

        // Then: retorna el DTO con id autogenerado y datos correctos
        assertThat(resultado).isNotNull();
        assertThat(resultado.getId()).isEqualTo(10);
        assertThat(resultado.getCodigoSala()).isEqualTo("A101");
        assertThat(resultado.getIdEdificio()).isEqualTo(1);
        assertThat(resultado.getNombreEdificio()).isEqualTo("Edificio A");
    }
}