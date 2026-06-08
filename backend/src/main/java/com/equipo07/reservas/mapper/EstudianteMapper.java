package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.EstudianteRequestDTO;
import com.equipo07.reservas.dto.EstudianteResponseDTO;
import com.equipo07.reservas.entity.Carrera;
import com.equipo07.reservas.entity.Estudiante;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {CarreraMapper.class})
public interface EstudianteMapper {

    @Mappings({
            @Mapping(source = "carrera.id", target = "idCarrera"),
            @Mapping(source = "carrera.nombreCarrera", target = "nombreCarrera")
    })
    EstudianteResponseDTO toResponse(Estudiante estudiante);

    @Mapping(source = "idCarrera", target = "carrera")
    Estudiante toEntity(EstudianteRequestDTO request);

    default Carrera map(Integer id) {
        if (id == null) return null;
        Carrera c = new Carrera();
        c.setId(id);
        return c;
    }
}