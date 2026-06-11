package com.equipo07.reservas.repository;

import com.equipo07.reservas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByEstudianteId(Integer estudianteId);
    List<Reserva> findBySalaId(Integer salaId);
}