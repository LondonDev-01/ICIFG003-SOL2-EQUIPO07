package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.entity.EstadoReserva;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Reserva;
import com.equipo07.reservas.entity.Sala;
import com.equipo07.reservas.exception.BusinessValidationException;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.ReservaService;
import com.equipo07.reservas.mapper.ReservaMapper;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.repository.EstadoReservaRepository;
import com.equipo07.reservas.repository.HorarioDisponibleRepository;
import com.equipo07.reservas.repository.ReservaRepository;
import com.equipo07.reservas.repository.SalaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservaServiceImpl implements ReservaService {

    private static final int OBSERVACION_MIN_LENGTH = 15;

    private final ReservaRepository reservaRepository;
    private final EstudianteRepository estudianteRepository;
    private final SalaRepository salaRepository;
    private final HorarioDisponibleRepository horarioDisponibleRepository;
    private final EstadoReservaRepository estadoReservaRepository;
    private final ReservaMapper reservaMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ReservaResponseDTO> listar() {
        return reservaRepository.findAll().stream()
                .map(reservaMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReservaResponseDTO obtenerPorId(Integer id) {
        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));
        return reservaMapper.toResponse(reserva);
    }

    @Override
    @Transactional
    public ReservaResponseDTO crear(ReservaRequestDTO request) {
        validarReglasDeNegocio(request);

        Estudiante estudiante = estudianteRepository.findById(request.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.getIdEstudiante()));

        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));

        HorarioDisponible horario = horarioDisponibleRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + request.getIdHorario()));

        EstadoReserva estado = estadoReservaRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + request.getIdEstado()));

        Reserva reserva = reservaMapper.toEntity(request);
        reserva.setEstudiante(estudiante);
        reserva.setSala(sala);
        reserva.setHorario(horario);
        reserva.setEstado(estado);
        return reservaMapper.toResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaResponseDTO actualizar(Integer id, ReservaRequestDTO request) {
        validarReglasDeNegocio(request);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        Estudiante estudiante = estudianteRepository.findById(request.getIdEstudiante())
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + request.getIdEstudiante()));

        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));

        HorarioDisponible horario = horarioDisponibleRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + request.getIdHorario()));

        EstadoReserva estado = estadoReservaRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + request.getIdEstado()));

        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setObservacion(request.getObservacion());
        reserva.setFechaCreacion(request.getFechaCreacion());
        reserva.setEstudiante(estudiante);
        reserva.setSala(sala);
        reserva.setHorario(horario);
        reserva.setEstado(estado);
        return reservaMapper.toResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!reservaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reserva no encontrada con id: " + id);
        }
        reservaRepository.deleteById(id);
    }

    private void validarReglasDeNegocio(ReservaRequestDTO request) {
        if (request.getFechaReserva() != null && request.getFechaReserva().isBefore(LocalDate.now())) {
            throw new BusinessValidationException("La fecha de reserva no puede ser anterior al día actual");
        }
        if (request.getObservacion() != null && request.getObservacion().trim().length() < OBSERVACION_MIN_LENGTH) {
            throw new BusinessValidationException("La observación debe tener al menos " + OBSERVACION_MIN_LENGTH + " caracteres");
        }
    }
}