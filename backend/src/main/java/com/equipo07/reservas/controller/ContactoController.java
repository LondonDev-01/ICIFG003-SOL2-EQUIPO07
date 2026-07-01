package com.equipo07.reservas.controller;

import com.equipo07.reservas.entity.Contacto;
import com.equipo07.reservas.repository.ContactoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/contacto")
@CrossOrigin(origins = "http://localhost:4200")
public class ContactoController {

    private final ContactoRepository contactoRepository;

    public ContactoController(ContactoRepository contactoRepository) {
        this.contactoRepository = contactoRepository;
    }

    @PostMapping
    public ResponseEntity<Contacto> crear(@RequestBody Contacto contacto) {
        if (contacto.getFechaEnvio() == null) {
            contacto.setFechaEnvio(LocalDateTime.now());
        }

        Contacto guardado = contactoRepository.save(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
    }
}