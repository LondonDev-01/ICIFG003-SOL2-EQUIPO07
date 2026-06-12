package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.SalaRequestDTO;
import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.interfaces.SalaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salas")
@CrossOrigin(origins = "http://localhost:4200")
public class SalaController {

    private final SalaService salaService;

    public SalaController(SalaService salaService) {
        this.salaService = salaService;
    }

    @GetMapping
    public ResponseEntity<List<SalaResponseDTO>> listar() {
        return ResponseEntity.ok(salaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(salaService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<SalaResponseDTO> crear(@Valid @RequestBody SalaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(salaService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalaResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody SalaRequestDTO request) {
        return ResponseEntity.ok(salaService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        salaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}