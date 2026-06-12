package com.equipo07.reservas.security;

import com.equipo07.reservas.repository.EstudianteRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String HEADER = "Authorization";
    private static final String PREFIX = "Bearer ";

    private final JwtService jwtService;
    private final EstudianteRepository estudianteRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(HEADER);
        if (header == null || !header.startsWith(PREFIX)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIX.length());
        try {
            Claims claims = jwtService.parse(token);
            String rut = claims.getSubject();
            Optional<com.equipo07.reservas.entity.Estudiante> estudianteOpt = estudianteRepository.findByRut(rut);
            if (estudianteOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = User.withUsername(rut)
                        .password(estudianteOpt.get().getPassword())
                        .authorities(AuthorityUtils.NO_AUTHORITIES)
                        .build();
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException ex) {
            // Token inválido o expirado, simplemente no autenticamos
        }

        chain.doFilter(request, response);
    }
}