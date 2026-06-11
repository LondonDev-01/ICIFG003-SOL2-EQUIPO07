package com.equipo07.reservas.interfaces;

import com.equipo07.reservas.dto.EdificioRequestDTO;
import com.equipo07.reservas.dto.EdificioResponseDTO;

import java.util.List;

public interface EdificioService {
    List<EdificioResponseDTO> listar();
    EdificioResponseDTO obtenerPorId(Integer id);
    EdificioResponseDTO crear(EdificioRequestDTO request);
    EdificioResponseDTO actualizar(Integer id, EdificioRequestDTO request);
    void eliminar(Integer id);
}