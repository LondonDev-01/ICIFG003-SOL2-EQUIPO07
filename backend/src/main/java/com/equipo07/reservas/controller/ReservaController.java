package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.interfaces.ReservaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(@Valid @RequestBody ReservaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody ReservaRequestDTO request) {
        return ResponseEntity.ok(reservaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}