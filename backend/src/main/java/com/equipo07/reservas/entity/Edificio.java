package com.equipo07.reservas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EDIFICIO")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Edificio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_edificio", length = 100, nullable = false)
    private String nombreEdificio;

    @Column(length = 200, nullable = false)
    private String direccion;

    // Relación 1:N con Sala
    @OneToMany(mappedBy = "edificio", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Sala> salas;
}