package com.equipo07.reservas.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "SALA")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Sala {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "codigo_sala", length = 20, unique = true)
    private String codigoSala;

    @Column(name = "nombre_sala", length = 100, nullable = false)
    private String nombreSala;

    @Column(nullable = false)
    private Integer capacidad;

    @Column(nullable = false)
    private Integer piso;

    @Column(length = 255, nullable = false)
    private String descripcion;

    @Column(length = 30, nullable = false)
    private String estado;

    // Relación N:1 con Edificio
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_edificio", nullable = false)
    private Edificio edificio;

    // Relación 1:N con Horario Disponible
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<HorarioDisponible> horariosDisponibles;

    // Relación 1:N con Reserva
    @OneToMany(mappedBy = "sala", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Reserva> reservas;
}