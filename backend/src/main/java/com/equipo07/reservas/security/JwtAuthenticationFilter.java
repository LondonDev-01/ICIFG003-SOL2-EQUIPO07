package com.equipo07.reservas.security;

import com.equipo07.reservas.entity.Estudiante;
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

/**
 * Filtro que extrae el JWT del header Authorization y setea el Estudiante en el SecurityContext.
 */
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
            System.out.println("RUT JWT = " + rut);
            Optional<Estudiante> estudianteOpt = estudianteRepository.findByRut(rut);
            System.out.println("USUARIO ENCONTRADO = " + estudianteOpt.isPresent());
            if (estudianteOpt.isPresent() && SecurityContextHolder.getContext().getAuthentication() == null) {
                Estudiante estudiante = estudianteOpt.get();
                UserDetails userDetails = User.withUsername(rut)
                        .password(estudiante.getPassword())
                        .authorities(AuthorityUtils.NO_AUTHORITIES)
                        .build();
                // Custom principal: el Estudiante completo, no solo el RUT
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        new EstudiantePrincipal(estudiante), null, userDetails.getAuthorities());
                System.out.println("AUTENTICANDO USUARIO");
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (JwtException ex) {
            // Token inválido o expirado, simplemente no autenticamos
        }

        chain.doFilter(request, response);
    }
}