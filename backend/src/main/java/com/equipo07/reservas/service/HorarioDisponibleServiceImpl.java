package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.HorarioDisponibleRequestDTO;
import com.equipo07.reservas.dto.HorarioDisponibleResponseDTO;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.HorarioDisponibleService;
import com.equipo07.reservas.mapper.HorarioDisponibleMapper;
import com.equipo07.reservas.repository.HorarioDisponibleRepository;
import com.equipo07.reservas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HorarioDisponibleServiceImpl implements HorarioDisponibleService {

    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final SalaRepository salaRepository;
    private final HorarioDisponibleMapper horarioDisponibleMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HorarioDisponibleResponseDTO> listar() {
        return horarioDisponibleRepository.findAll().stream()
                .map(horarioDisponibleMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public HorarioDisponibleResponseDTO obtenerPorId(Integer id) {
        HorarioDisponible horario = horarioDisponibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));
        return horarioDisponibleMapper.toResponse(horario);
    }

    @Override
    @Transactional
    public HorarioDisponibleResponseDTO crear(HorarioDisponibleRequestDTO request) {
        if (request.getHoraTermino().isBefore(request.getHoraInicio()) || request.getHoraTermino().equals(request.getHoraInicio())) {
            throw new RuntimeException("La hora de término debe ser posterior a la hora de inicio");
        }
        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));
        HorarioDisponible horario = horarioDisponibleMapper.toEntity(request);
        horario.setSala(sala);
        return horarioDisponibleMapper.toResponse(horarioDisponibleRepository.save(horario));
    }

    @Override
    @Transactional
    public HorarioDisponibleResponseDTO actualizar(Integer id, HorarioDisponibleRequestDTO request) {
        HorarioDisponible horario = horarioDisponibleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + id));

        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));

        horario.setHoraInicio(request.getHoraInicio());
        horario.setHoraTermino(request.getHoraTermino());
        horario.setSala(sala);
        return horarioDisponibleMapper.toResponse(horarioDisponibleRepository.save(horario));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!horarioDisponibleRepository.existsById(id)) {
            throw new ResourceNotFoundException("Horario no encontrado con id: " + id);
        }
        horarioDisponibleRepository.deleteById(id);
    }
}