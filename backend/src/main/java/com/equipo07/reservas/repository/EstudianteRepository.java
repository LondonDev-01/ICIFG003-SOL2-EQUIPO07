package com.equipo07.reservas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.equipo07.reservas.entity.Estudiante;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstudianteRepository extends JpaRepository<Estudiante, Integer> {
    Optional<Estudiante> findByRut(String rut);
    Optional<Estudiante> findByCorreo(String correo);
}