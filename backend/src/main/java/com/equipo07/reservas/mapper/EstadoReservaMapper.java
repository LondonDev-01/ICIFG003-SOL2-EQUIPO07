package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.EstadoReservaRequestDTO;
import com.equipo07.reservas.dto.EstadoReservaResponseDTO;
import com.equipo07.reservas.entity.EstadoReserva;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EstadoReservaMapper {
    EstadoReservaResponseDTO toResponse(EstadoReserva estado);
    EstadoReserva toEntity(EstadoReservaRequestDTO request);
}