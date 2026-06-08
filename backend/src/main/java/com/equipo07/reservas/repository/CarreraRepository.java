package com.equipo07.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.equipo07.reservas.entity.Carrera;
import org.springframework.stereotype.Repository;

@Repository
public interface CarreraRepository extends JpaRepository<Carrera, Integer> {
}