package com.equipo07.reservas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "CARRERA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Carrera {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre_carrera", length = 100, nullable = false)
    private String nombreCarrera;

    @Column(length = 100, nullable = false)
    private String facultad;

    // Relación 1:N con Estudiante
    @OneToMany(mappedBy = "carrera", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Estudiante> estudiantes;
}