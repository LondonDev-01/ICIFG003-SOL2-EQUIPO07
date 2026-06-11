package com.equipo07.reservas.repository;

import com.equipo07.reservas.entity.Sala;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SalaRepository extends JpaRepository<Sala, Integer> {
    Optional<Sala> findByCodigoSala(String codigoSala);
}