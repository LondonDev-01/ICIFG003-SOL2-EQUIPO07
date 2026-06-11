package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;

import java.util.List;

public interface ReservaService {
    List<ReservaResponseDTO> listar();
    ReservaResponseDTO obtenerPorId(Integer id);
    ReservaResponseDTO crear(ReservaRequestDTO request);
    ReservaResponseDTO actualizar(Integer id, ReservaRequestDTO request);
    void eliminar(Integer id);
}