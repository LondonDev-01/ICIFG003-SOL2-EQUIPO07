package com.equipo07.reservas.repository;

import com.equipo07.reservas.entity.Contacto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactoRepository extends JpaRepository<Contacto, Integer> {
}