package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.EstadoReservaRequestDTO;
import com.equipo07.reservas.dto.EstadoReservaResponseDTO;
import com.equipo07.reservas.entity.EstadoReserva;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.EstadoReservaService;
import com.equipo07.reservas.mapper.EstadoReservaMapper;
import com.equipo07.reservas.repository.EstadoReservaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstadoReservaServiceImpl implements EstadoReservaService {

    private final EstadoReservaRepository estadoReservaRepository;
    private final EstadoReservaMapper estadoReservaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstadoReservaResponseDTO> listar() {
        return estadoReservaRepository.findAll().stream()
                .map(estadoReservaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstadoReservaResponseDTO obtenerPorId(Integer id) {
        EstadoReserva estado = estadoReservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + id));
        return estadoReservaMapper.toResponse(estado);
    }

    @Override
    @Transactional
    public EstadoReservaResponseDTO crear(EstadoReservaRequestDTO request) {
        if (estadoReservaRepository.findByNombreEstado(request.getNombreEstado()).isPresent()) {
            throw new RuntimeException("Ya existe un estado de reserva con el nombre: " + request.getNombreEstado());
        }
        EstadoReserva estado = estadoReservaMapper.toEntity(request);
        return estadoReservaMapper.toResponse(estadoReservaRepository.save(estado));
    }

    @Override
    @Transactional
    public EstadoReservaResponseDTO actualizar(Integer id, EstadoReservaRequestDTO request) {
        EstadoReserva estado = estadoReservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + id));
        estado.setNombreEstado(request.getNombreEstado());
        return estadoReservaMapper.toResponse(estadoReservaRepository.save(estado));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!estadoReservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estado de reserva no encontrado con id: " + id);
        }
        estadoReservaRepository.deleteById(id);
    }
}