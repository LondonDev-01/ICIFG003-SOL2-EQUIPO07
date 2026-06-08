package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.CarreraRequestDTO;
import com.equipo07.reservas.dto.CarreraResponseDTO;

import java.util.List;

public interface CarreraService {
    List<CarreraResponseDTO> listar();
    CarreraResponseDTO obtenerPorId(Integer id);
    CarreraResponseDTO crear(CarreraRequestDTO request);
    CarreraResponseDTO actualizar(Integer id, CarreraRequestDTO request);
    void eliminar(Integer id);
}