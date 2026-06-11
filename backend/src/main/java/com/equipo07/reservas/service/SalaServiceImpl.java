package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.SalaRequestDTO;
import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.SalaService;
import com.equipo07.reservas.mapper.SalaMapper;
import com.equipo07.reservas.repository.EdificioRepository;
import com.equipo07.reservas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;
    private final EdificioRepository edificioRepository;
    private final SalaMapper salaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<SalaResponseDTO> listar() {
        return salaRepository.findAll().stream()
                .map(salaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public SalaResponseDTO obtenerPorId(Integer id) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));
        return salaMapper.toResponse(sala);
    }

    @Override
    @Transactional
    public SalaResponseDTO crear(SalaRequestDTO request) {
        Edificio edificio = edificioRepository.findById(request.getIdEdificio())
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado con id: " + request.getIdEdificio()));

        Sala sala = salaMapper.toEntity(request);
        sala.setEdificio(edificio);
        return salaMapper.toResponse(salaRepository.save(sala));
    }

    @Override
    @Transactional
    public SalaResponseDTO actualizar(Integer id, SalaRequestDTO request) {
        Sala sala = salaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + id));

        Edificio edificio = edificioRepository.findById(request.getIdEdificio())
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado con id: " + request.getIdEdificio()));

        sala.setCodigoSala(request.getCodigoSala());
        sala.setNombreSala(request.getNombreSala());
        sala.setCapacidad(request.getCapacidad());
        sala.setPiso(request.getPiso());
        sala.setDescripcion(request.getDescripcion());
        sala.setEstado(request.getEstado());
        sala.setEdificio(edificio);
        return salaMapper.toResponse(salaRepository.save(sala));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!salaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Sala no encontrada con id: " + id);
        }
        salaRepository.deleteById(id);
    }
}