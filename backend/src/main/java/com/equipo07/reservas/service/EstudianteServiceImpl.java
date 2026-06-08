package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.EstudianteRequestDTO;
import com.equipo07.reservas.dto.EstudianteResponseDTO;
import com.equipo07.reservas.entity.Carrera;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.EstudianteService;
import com.equipo07.reservas.mapper.EstudianteMapper;
import com.equipo07.reservas.repository.CarreraRepository;
import com.equipo07.reservas.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstudianteServiceImpl implements EstudianteService {

    private final EstudianteRepository estudianteRepository;
    private final CarreraRepository carreraRepository;
    private final EstudianteMapper estudianteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EstudianteResponseDTO> listar() {
        return estudianteRepository.findAll().stream()
                .map(estudianteMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EstudianteResponseDTO obtenerPorId(Integer id) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));
        return estudianteMapper.toResponse(estudiante);
    }

    @Override
    @Transactional
    public EstudianteResponseDTO crear(EstudianteRequestDTO request) {
        if (request.getRut() != null && estudianteRepository.findByRut(request.getRut()).isPresent()) {
            throw new RuntimeException("Ya existe un estudiante con el rut: " + request.getRut());
        }
        if (request.getCorreo() != null && estudianteRepository.findByCorreo(request.getCorreo()).isPresent()) {
            throw new RuntimeException("Ya existe un estudiante con el correo: " + request.getCorreo());
        }

        Carrera carrera = carreraRepository.findById(request.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + request.getIdCarrera()));

        Estudiante estudiante = estudianteMapper.toEntity(request);
        estudiante.setCarrera(carrera);
        return estudianteMapper.toResponse(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional
    public EstudianteResponseDTO actualizar(Integer id, EstudianteRequestDTO request) {
        Estudiante estudiante = estudianteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado con id: " + id));

        Carrera carrera = carreraRepository.findById(request.getIdCarrera())
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + request.getIdCarrera()));

        estudiante.setRut(request.getRut());
        estudiante.setNombre(request.getNombre());
        estudiante.setApellido(request.getApellido());
        estudiante.setCorreo(request.getCorreo());
        estudiante.setTelefono(request.getTelefono());
        estudiante.setFechaRegistro(request.getFechaRegistro());
        estudiante.setCarrera(carrera);
        return estudianteMapper.toResponse(estudianteRepository.save(estudiante));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!estudianteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Estudiante no encontrado con id: " + id);
        }
        estudianteRepository.deleteById(id);
    }
}