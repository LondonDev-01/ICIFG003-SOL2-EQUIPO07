package com.equipo07.reservas.controller;

import com.equipo07.reservas.dto.ReservaRequestDTO;
import com.equipo07.reservas.dto.ReservaResponseDTO;
import com.equipo07.reservas.entity.Estudiante;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.ReservaService;
import com.equipo07.reservas.repository.EstudianteRepository;
import com.equipo07.reservas.repository.ReservaRepository;
import com.equipo07.reservas.security.EstudiantePrincipal;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public ResponseEntity<List<ReservaResponseDTO>> misReservas(@AuthenticationPrincipal EstudiantePrincipal principal) {

        System.out.println("=== MIS RESERVAS ===");
        System.out.println("PRINCIPAL: " + principal);

        Estudiante estudiante = principal.estudiante();

        System.out.println("ESTUDIANTE: " + estudiante);
        System.out.println("ID ESTUDIANTE: " + estudiante.getId());

        List<ReservaResponseDTO> reservas = reservaRepository.findByEstudianteId(estudiante.getId())
                .stream()
                .map(r -> reservaService.obtenerPorId(r.getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reservas);
    }

    @PostMapping
    public ResponseEntity<ReservaResponseDTO> crear(
            @Valid @RequestBody ReservaRequestDTO request,
            @AuthenticationPrincipal EstudiantePrincipal principal) {

        System.out.println("ENTRO AL CONTROLLER");
        System.out.println("PRINCIPAL = " + principal);

        Integer idEstudiante = principal != null
                ? principal.estudiante().getId()
                : null;

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(reservaService.crear(request, idEstudiante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservaResponseDTO> actualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ReservaRequestDTO request,
            @AuthenticationPrincipal EstudiantePrincipal principal) {

        System.out.println("EDITANDO RESERVA");
        System.out.println("PRINCIPAL = " + principal);

        return ResponseEntity.ok(
                reservaService.actualizar(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        reservaService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}