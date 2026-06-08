package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.CarreraRequestDTO;
import com.equipo07.reservas.dto.CarreraResponseDTO;
import com.equipo07.reservas.entity.Carrera;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CarreraMapper {
    CarreraResponseDTO toResponse(Carrera carrera);
    Carrera toEntity(CarreraRequestDTO request);
}