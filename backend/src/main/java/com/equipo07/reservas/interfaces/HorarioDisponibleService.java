package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.HorarioDisponibleRequestDTO;
import com.equipo07.reservas.dto.HorarioDisponibleResponseDTO;

import java.util.List;

public interface HorarioDisponibleService {
    List<HorarioDisponibleResponseDTO> listar();
    HorarioDisponibleResponseDTO obtenerPorId(Integer id);
    HorarioDisponibleResponseDTO crear(HorarioDisponibleRequestDTO request);
    HorarioDisponibleResponseDTO actualizar(Integer id, HorarioDisponibleRequestDTO request);
    void eliminar(Integer id);
}