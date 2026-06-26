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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReservaServiceImpl implements ReservaService {

    private static final int OBSERVACION_MIN_LENGTH = 15;
    private static final String SALA_ESTADO_DISPONIBLE = "Disponible";
    private static final String ESTADO_RESERVA_CONFIRMADA = "Confirmada";
    private static final String ESTADO_RESERVA_PENDIENTE = "Pendiente";

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
    public ReservaResponseDTO crear(ReservaRequestDTO request, Integer idEstudianteAutenticado) {
        validarReglasDeNegocio(request);

        // Prioridad: idEstudiante explícito (del JWT) sobre el del body
        Integer idEstudiante = idEstudianteAutenticado != null
                ? idEstudianteAutenticado
                : request.getIdEstudiante();
        if (idEstudiante == null) {
            throw new BusinessValidationException("No se pudo determinar el estudiante autenticado");
        }

        Estudiante estudiante = estudianteRepository.findById(idEstudiante)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + idEstudiante));

        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));

        HorarioDisponible horario = horarioDisponibleRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + request.getIdHorario()));

        EstadoReserva estado = estadoReservaRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + request.getIdEstado()));

        validarDisponibilidadSala(sala, horario, request, null);
        validarDisponibilidadEstudiante(estudiante, request, null);

        log.info("Creando reserva para estudiante {} en sala {} y horario {}", estudiante.getId(), sala.getId(), horario.getId());

        Reserva reserva = reservaMapper.toEntity(request);
        // El mapper ya setea el FK del Estudiante; nos aseguramos de reescribirlo con el del JWT
        reserva.setEstudiante(estudiante);
        reserva.setSala(sala);
        reserva.setHorario(horario);
        reserva.setEstado(estado);
        // Asegurar que fechaCreacion esté seteada
        if (reserva.getFechaCreacion() == null) {
            reserva.setFechaCreacion(java.time.LocalDateTime.now());
        }
        return reservaMapper.toResponse(reservaRepository.save(reserva));
    }

    @Override
    @Transactional
    public ReservaResponseDTO actualizar(Integer id, ReservaRequestDTO request) {
        validarReglasDeNegocio(request);

        Reserva reserva = reservaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reserva no encontrada con id: " + id));

        // Para actualizar mantenemos el estudiante de la reserva original (no se puede cambiar de estudiante)
        Estudiante estudiante = reserva.getEstudiante();
        if (estudiante == null) {
            throw new ResourceNotFoundException("La reserva no tiene estudiante asociado");
        }

        Sala sala = salaRepository.findById(request.getIdSala())
                .orElseThrow(() -> new ResourceNotFoundException("Sala no encontrada con id: " + request.getIdSala()));

        HorarioDisponible horario = horarioDisponibleRepository.findById(request.getIdHorario())
                .orElseThrow(() -> new ResourceNotFoundException("Horario no encontrado con id: " + request.getIdHorario()));

        EstadoReserva estado = estadoReservaRepository.findById(request.getIdEstado())
                .orElseThrow(() -> new ResourceNotFoundException("Estado de reserva no encontrado con id: " + request.getIdEstado()));

        // Excluir la reserva actual de las verificaciones para no auto-conflictuar
        validarDisponibilidadSala(sala, horario, request, id);
        validarDisponibilidadEstudiante(estudiante, request, id);

        log.info("Actualizando reserva {} para estudiante {}", id, estudiante.getId());

        reserva.setFechaReserva(request.getFechaReserva());
        reserva.setObservacion(request.getObservacion());
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

    private void validarDisponibilidadSala(Sala sala, HorarioDisponible horario, ReservaRequestDTO request, Integer excludeReservaId) {
        // Regla 3: la sala debe estar "Disponible"
        if (!SALA_ESTADO_DISPONIBLE.equalsIgnoreCase(sala.getEstado())) {
            throw new BusinessValidationException(
                    "La sala " + sala.getId() + " no está disponible para reservar (estado actual: " + sala.getEstado() + ")");
        }

        // Regla 1: no doble booking de (sala, horario, fecha) con estado Confirmada o Pendiente
        if (salaHorarioFechaOcupados(sala.getId(), horario.getId(), request.getFechaReserva(), excludeReservaId)) {
            throw new BusinessValidationException(
                    "La sala " + sala.getId() + " ya está reservada para la fecha " + request.getFechaReserva()
                            + " en el horario seleccionado");
        }
    }

    private void validarDisponibilidadEstudiante(Estudiante estudiante, ReservaRequestDTO request, Integer excludeReservaId) {
            log.debug("Validando disponibilidad de estudiante {} para fecha {}", estudiante.getId(), request.getFechaReserva());

    	boolean existePendiente =
    	        reservaRepository.existsByEstudianteIdAndFechaReservaAndEstadoNombreEstado(
    	                estudiante.getId(),
    	                request.getFechaReserva(),
    	                ESTADO_RESERVA_PENDIENTE);

    	boolean existeConfirmada =
    	        reservaRepository.existsByEstudianteIdAndFechaReservaAndEstadoNombreEstado(
    	                estudiante.getId(),
    	                request.getFechaReserva(),
    	                ESTADO_RESERVA_CONFIRMADA);

            log.debug("Reservas activas del estudiante {} - pendiente={}, confirmada={}, excludeReservaId={}",
                    estudiante.getId(), existePendiente, existeConfirmada, excludeReservaId);

    	boolean existe = estudianteTieneReservaEnFecha(
    	        estudiante.getId(),
    	        request.getFechaReserva(),
    	        excludeReservaId);

            log.debug("Existe reserva activa para estudiante {} en fecha {}: {}", estudiante.getId(), request.getFechaReserva(), existe);
        // Regla 2: el estudiante no puede tener otra reserva activa en la misma fecha
    	if (existe) {
    	    throw new IllegalStateException(
    	        "Ya tienes una reserva activa para esta fecha. Solo puedes reservar una sala por día."
    	    );
    	}
    }

    private boolean salaHorarioFechaOcupados(
            Integer salaId,
            Integer horarioId,
            LocalDate fecha,
            Integer excludeReservaId) {

        if (excludeReservaId == null) {

            return reservaRepository
                    .existsBySalaIdAndHorarioIdAndFechaReservaAndEstadoNombreEstado(
                            salaId,
                            horarioId,
                            fecha,
                            ESTADO_RESERVA_CONFIRMADA)

                    ||

                    reservaRepository
                    .existsBySalaIdAndHorarioIdAndFechaReservaAndEstadoNombreEstado(
                            salaId,
                            horarioId,
                            fecha,
                            ESTADO_RESERVA_PENDIENTE);
        }

        return reservaRepository
                .existsBySalaIdAndHorarioIdAndFechaReservaAndEstadoNombreEstadoAndIdNot(
                        salaId,
                        horarioId,
                        fecha,
                        ESTADO_RESERVA_CONFIRMADA,
                        excludeReservaId)

                ||

                reservaRepository
                .existsBySalaIdAndHorarioIdAndFechaReservaAndEstadoNombreEstadoAndIdNot(
                        salaId,
                        horarioId,
                        fecha,
                        ESTADO_RESERVA_PENDIENTE,
                        excludeReservaId);
    }

    private boolean estudianteTieneReservaEnFecha(
            Integer estudianteId,
            LocalDate fecha,
            Integer excludeReservaId) {

        if (excludeReservaId == null) {

            return reservaRepository
                    .existsByEstudianteIdAndFechaReservaAndEstadoNombreEstado(
                            estudianteId,
                            fecha,
                            ESTADO_RESERVA_CONFIRMADA)

                    ||

                    reservaRepository
                    .existsByEstudianteIdAndFechaReservaAndEstadoNombreEstado(
                            estudianteId,
                            fecha,
                            ESTADO_RESERVA_PENDIENTE);
        }

        return reservaRepository
                .existsByEstudianteIdAndFechaReservaAndEstadoNombreEstadoAndIdNot(
                        estudianteId,
                        fecha,
                        ESTADO_RESERVA_CONFIRMADA,
                        excludeReservaId)

                ||

                reservaRepository
                .existsByEstudianteIdAndFechaReservaAndEstadoNombreEstadoAndIdNot(
                        estudianteId,
                        fecha,
                        ESTADO_RESERVA_PENDIENTE,
                        excludeReservaId);
    }
}