package com.equipo07.reservas.security;

import com.equipo07.reservas.repository.EstudianteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final EstudianteRepository estudianteRepository;

    @Override
    public UserDetails loadUserByUsername(String rut) throws UsernameNotFoundException {
        return estudianteRepository.findByRut(rut)
                .map(estudiante -> User.withUsername(estudiante.getRut())
                        .password(estudiante.getPassword())
                        .authorities(AuthorityUtils.NO_AUTHORITIES)
                        .build())
                .orElseThrow(() -> new UsernameNotFoundException("Estudiante no encontrado con RUT: " + rut));
    }
}