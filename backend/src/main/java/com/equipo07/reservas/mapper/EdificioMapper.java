package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.EdificioRequestDTO;
import com.equipo07.reservas.dto.EdificioResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EdificioMapper {
    EdificioResponseDTO toResponse(Edificio edificio);
    Edificio toEntity(EdificioRequestDTO request);
}