package com.equipo07.reservas.mapper;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.entity.EstadoReserva;
import com.equipo07.reservas.entity.HorarioDisponible;
import com.equipo07.reservas.entity.Reserva;
import com.equipo07.reservas.entity.Sala;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface ReservaMapper {

    @Mappings({
            @Mapping(source = "estudiante.id", target = "idEstudiante"),
            @Mapping(source = "estudiante.nombre", target = "nombreEstudiante"),
            @Mapping(source = "sala.id", target = "idSala"),
            @Mapping(source = "sala.nombreSala", target = "nombreSala"),
            @Mapping(source = "horario.id", target = "idHorario"),
            @Mapping(source = "horario.horaInicio", target = "bloqueHorario", dateFormat = "HH:mm"),
            @Mapping(source = "estado.idEstado", target = "idEstado"),
            @Mapping(source = "estado.nombreEstado", target = "nombreEstado")
    })
    ReservaResponseDTO toResponse(Reserva reserva);

    @Mappings({
            @Mapping(source = "idEstudiante", target = "estudiante"),
            @Mapping(source = "idSala", target = "sala"),
            @Mapping(source = "idHorario", target = "horario"),
            @Mapping(source = "idEstado", target = "estado")
    })
    Reserva toEntity(ReservaRequestDTO request);

    default Estudiante mapEstudiante(Integer id) {
        if (id == null) return null;
        Estudiante e = new Estudiante();
        e.setId(id);
        return e;
    }

    default Sala mapSala(Integer id) {
        if (id == null) return null;
        Sala s = new Sala();
        s.setId(id);
        return s;
    }

    default HorarioDisponible mapHorario(Integer id) {
        if (id == null) return null;
        HorarioDisponible h = new HorarioDisponible();
        h.setId(id);
        return h;
    }

    default EstadoReserva mapEstado(Integer id) {
        if (id == null) return null;
        EstadoReserva e = new EstadoReserva();
        e.setIdEstado(id);
        return e;
    }
}