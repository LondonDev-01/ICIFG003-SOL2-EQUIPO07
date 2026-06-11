package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.HorarioDisponibleRequestDTO;
import com.equipo07.reservas.dto.HorarioDisponibleResponseDTO;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface HorarioDisponibleMapper {

    @Mappings({
            @Mapping(source = "sala.id", target = "idSala"),
            @Mapping(source = "sala.nombreSala", target = "nombreSala")
    })
    HorarioDisponibleResponseDTO toResponse(HorarioDisponible horario);

    @Mapping(source = "idSala", target = "sala")
    HorarioDisponible toEntity(HorarioDisponibleRequestDTO request);

    default Sala map(Integer id) {
        if (id == null) return null;
        Sala s = new Sala();
        s.setId(id);
        return s;
    }
}