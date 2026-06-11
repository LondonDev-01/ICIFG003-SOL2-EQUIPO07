package com.equipo07.reservas.repository;

import com.equipo07.reservas.entity.HorarioDisponible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HorarioDisponibleRepository extends JpaRepository<HorarioDisponible, Integer> {
    List<HorarioDisponible> findBySalaId(Integer salaId);
}