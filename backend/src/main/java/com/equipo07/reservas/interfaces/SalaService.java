package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.SalaRequestDTO;
import com.equipo07.reservas.dto.SalaResponseDTO;

import java.util.List;

public interface SalaService {
    List<SalaResponseDTO> listar();
    SalaResponseDTO obtenerPorId(Integer id);
    SalaResponseDTO crear(SalaRequestDTO request);
    SalaResponseDTO actualizar(Integer id, SalaRequestDTO request);
    void eliminar(Integer id);
}