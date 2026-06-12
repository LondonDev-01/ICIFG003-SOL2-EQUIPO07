package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.ReservaService;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.repository.ReservaRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reservas")
@CrossOrigin(origins = "http://localhost:4200")
public class ReservaController {

    private final ReservaService reservaService;
    private final EstudianteRepository estudianteRepository;
    private final ReservaRepository reservaRepository;

    public ReservaController(ReservaService reservaService,
                             EstudianteRepository estudianteRepository,
                             ReservaRepository reservaRepository) {
        this.reservaService = reservaService;
        this.estudianteRepository = estudianteRepository;
        this.reservaRepository = reservaRepository;
    }

    @GetMapping
    public ResponseEntity<List<ReservaResponseDTO>> listar() {
        return ResponseEntity.ok(reservaService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> obtenerPorId(@PathVariable Integer id) {
        return ResponseEntity.ok(reservaService.obtenerPorId(id));
    }

    @GetMapping("/mis-reservas")
    public ResponseEntity<List<ReservaResponseDTO>> misReservas() {
        String rut = currentUserRut();
        Estudiante estudiante = estudianteRepository.findByRut(rut)
                .orElseThrow(() -> new ResourceNotFoundException("Estudiante no encontrado: " + rut));
        List<ReservaResponseDTO> reservas = reservaRepository.findByEstudianteId(estudiante.getId())
                .stream()
                .map(r -> reservaService.obtenerPorId(r.getId()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservas);
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

    private String currentUserRut() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            throw new ResourceNotFoundException("No hay usuario autenticado");
        }
        return auth.getName();
    }
}