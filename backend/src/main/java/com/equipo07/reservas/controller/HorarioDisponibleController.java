package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.HorarioDisponibleRequestDTO;
import com.equipo07.reservas.dto.HorarioDisponibleResponseDTO;
import com.equipo07.reservas.interfaces.HorarioDisponibleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/horarios")
public class HorarioDisponibleController {

    private final HorarioDisponibleService horarioDisponibleService;

    public HorarioDisponibleController(HorarioDisponibleService horarioDisponibleService) {
        this.horarioDisponibleService = horarioDisponibleService;
    }

    @GetMapping
    public ResponseEntity<List<HorarioDisponibleResponseDTO>> listar() {
        return ResponseEntity.ok(horarioDisponibleService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<HorarioDisponibleResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(horarioDisponibleService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<HorarioDisponibleResponseDTO> crear(@Valid @RequestBody HorarioDisponibleRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(horarioDisponibleService.crear(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<HorarioDisponibleResponseDTO> actualizar(@PathVariable Integer id, @Valid @RequestBody HorarioDisponibleRequestDTO request) {
        return ResponseEntity.ok(horarioDisponibleService.actualizar(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        horarioDisponibleService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}