package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.EstadoReservaRequestDTO;
import com.equipo07.reservas.dto.EstadoReservaResponseDTO;
import com.equipo07.reservas.interfaces.EstadoReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados-reserva")
public class EstadoReservaController {

    private final EstadoReservaService estadoReservaService;

    public EstadoReservaController(EstadoReservaService estadoReservaService) {
        this.estadoReservaService = estadoReservaService;
    }

    @GetMapping
    public ResponseEntity<List<EstadoReservaResponseDTO>> listar() {
        return ResponseEntity.ok(estadoReservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EstadoReservaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(estadoReservaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EstadoReservaResponseDTO> crear(@Valid @RequestBody EstadoReservaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estadoReservaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EstadoReservaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EstadoReservaRequestDTO request) {
        return ResponseEntity.ok(estadoReservaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        estadoReservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}