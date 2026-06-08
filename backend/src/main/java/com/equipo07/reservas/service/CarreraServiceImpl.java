package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.CarreraRequestDTO;
import com.equipo07.reservas.dto.CarreraResponseDTO;
import com.equipo07.reservas.entity.Carrera;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.CarreraService;
import com.equipo07.reservas.mapper.CarreraMapper;
import com.equipo07.reservas.repository.CarreraRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarreraServiceImpl implements CarreraService {

    private final CarreraRepository carreraRepository;
    private final CarreraMapper carreraMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CarreraResponseDTO> listar() {
        return carreraRepository.findAll().stream()
                .map(carreraMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CarreraResponseDTO obtenerPorId(Integer id) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + id));
        return carreraMapper.toResponse(carrera);
    }

    @Override
    @Transactional
    public CarreraResponseDTO crear(CarreraRequestDTO request) {
        Carrera carrera = carreraMapper.toEntity(request);
        Carrera guardada = carreraRepository.save(carrera);
        return carreraMapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public CarreraResponseDTO actualizar(Integer id, CarreraRequestDTO request) {
        Carrera carrera = carreraRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Carrera no encontrada con id: " + id));
        carrera.setNombreCarrera(request.getNombreCarrera());
        carrera.setFacultad(request.getFacultad());
        return carreraMapper.toResponse(carreraRepository.save(carrera));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!carreraRepository.existsById(id)) {
            throw new ResourceNotFoundException("Carrera no encontrada con id: " + id);
        }
        carreraRepository.deleteById(id);
    }
}