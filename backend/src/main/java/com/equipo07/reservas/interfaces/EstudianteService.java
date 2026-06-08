package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.EstudianteRequestDTO;
import com.equipo07.reservas.dto.EstudianteResponseDTO;

import java.util.List;

public interface EstudianteService {
    List<EstudianteResponseDTO> listar();
    EstudianteResponseDTO obtenerPorId(Integer id);
    EstudianteResponseDTO crear(EstudianteRequestDTO request);
    EstudianteResponseDTO actualizar(Integer id, EstudianteRequestDTO request);
    void eliminar(Integer id);
}