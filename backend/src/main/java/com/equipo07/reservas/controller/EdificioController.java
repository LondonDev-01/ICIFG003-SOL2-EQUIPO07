package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.EdificioRequestDTO;
import com.equipo07.reservas.dto.EdificioResponseDTO;
import com.equipo07.reservas.interfaces.EdificioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/edificios")
public class EdificioController {

    private final EdificioService edificioService;

    public EdificioController(EdificioService edificioService) {
        this.edificioService = edificioService;
    }

    @GetMapping
    public ResponseEntity<List<EdificioResponseDTO>> listar() {
        return ResponseEntity.ok(edificioService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EdificioResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(edificioService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<EdificioResponseDTO> crear(@Valid @RequestBody EdificioRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(edificioService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EdificioResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody EdificioRequestDTO request) {
        return ResponseEntity.ok(edificioService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        edificioService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}