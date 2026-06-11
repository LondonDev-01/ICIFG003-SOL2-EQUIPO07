package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.EstadoReservaRequestDTO;
import com.equipo07.reservas.dto.EstadoReservaResponseDTO;

import java.util.List;

public interface EstadoReservaService {
    List<EstadoReservaResponseDTO> listar();
    EstadoReservaResponseDTO obtenerPorId(Integer id);
    EstadoReservaResponseDTO crear(EstadoReservaRequestDTO request);
    EstadoReservaResponseDTO actualizar(Integer id, EstadoReservaRequestDTO request);
    void eliminar(Integer id);
}