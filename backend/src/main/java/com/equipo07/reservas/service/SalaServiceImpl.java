package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.SalaResponseDTO;
import com.equipo07.reservas.interfaces.SalaService;
import com.equipo07.reservas.mapper.SalaMapper;
import com.equipo07.reservas.repository.SalaRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SalaServiceImpl implements SalaService {

    private final SalaRepository salaRepository;
    private final SalaMapper salaMapper;

    public SalaServiceImpl(SalaRepository salaRepository, SalaMapper salaMapper) {
        this.salaRepository = salaRepository;
        this.salaMapper = salaMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<SalaResponseDTO> listar() {
        return salaRepository.findAll()
                .stream()
                .map(salaMapper::toResponse)
                .collect(Collectors.toList());
    }
}