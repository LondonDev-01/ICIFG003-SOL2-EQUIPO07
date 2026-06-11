package com.equipo07.reservas.service;

import com.equipo07.reservas.dto.EdificioRequestDTO;
import com.equipo07.reservas.dto.EdificioResponseDTO;
import com.equipo07.reservas.entity.Edificio;
import com.equipo07.reservas.exception.ResourceNotFoundException;
import com.equipo07.reservas.interfaces.EdificioService;
import com.equipo07.reservas.mapper.EdificioMapper;
import com.equipo07.reservas.repository.EdificioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EdificioServiceImpl implements EdificioService {

    private final EdificioRepository edificioRepository;
    private final EdificioMapper edificioMapper;

    @Override
    @Transactional(readOnly = true)
    public List<EdificioResponseDTO> listar() {
        return edificioRepository.findAll().stream()
                .map(edificioMapper::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public EdificioResponseDTO obtenerPorId(Integer id) {
        Edificio edificio = edificioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado con id: " + id));
        return edificioMapper.toResponse(edificio);
    }

    @Override
    @Transactional
    public EdificioResponseDTO crear(EdificioRequestDTO request) {
        Edificio edificio = edificioMapper.toEntity(request);
        Edificio guardada = edificioRepository.save(edificio);
        return edificioMapper.toResponse(guardada);
    }

    @Override
    @Transactional
    public EdificioResponseDTO actualizar(Integer id, EdificioRequestDTO request) {
        Edificio edificio = edificioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Edificio no encontrado con id: " + id));
        edificio.setNombreEdificio(request.getNombreEdificio());
        edificio.setDireccion(request.getDireccion());
        return edificioMapper.toResponse(edificioRepository.save(edificio));
    }

    @Override
    @Transactional
    public void eliminar(Integer id) {
        if (!edificioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Edificio no encontrado con id: " + id);
        }
        edificioRepository.deleteById(id);
    }
}