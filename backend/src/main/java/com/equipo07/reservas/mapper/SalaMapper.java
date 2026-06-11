package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.SalaRequestDTO;
import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import com.equipo07.reservas.entity.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface SalaMapper {

    @Mappings({
            @Mapping(source = "edificio.id", target = "idEdificio"),
            @Mapping(source = "edificio.nombreEdificio", target = "nombreEdificio")
    })
    SalaResponseDTO toResponse(Sala sala);

    @Mapping(source = "idEdificio", target = "edificio")
    Sala toEntity(SalaRequestDTO request);

    default Edificio map(Integer id) {
        if (id == null) return null;
        Edificio e = new Edificio();
        e.setId(id);
        return e;
    }
}