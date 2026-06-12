package com.equipo07.reservas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ESTUDIANTE")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 12, unique = true)
    private String rut;

    @Column(length = 100, nullable = false)
    private String nombre;

    @Column(length = 100, nullable = false)
    private String apellido;

    @Column(length = 150, unique = true)
    private String correo;

    @Column(length = 20)
    private String telefono;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDate fechaRegistro;

    @Column(length = 60)
    private String password;

    // Relación N:1 con Carrera
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_carrera", nullable = false)
    private Carrera carrera;

    // Relación 1:N con Reserva
    @OneToMany(mappedBy = "estudiante", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reserva> reservas;
}