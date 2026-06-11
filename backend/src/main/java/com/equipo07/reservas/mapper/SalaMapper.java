package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.entity.Sala;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SalaMapper {

    SalaResponseDTO toResponse(Sala sala);

}