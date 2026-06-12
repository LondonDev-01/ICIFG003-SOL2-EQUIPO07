package com.equipo07.reservas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtServiceTest {

    private final JwtService jwtService = new JwtService(
            "test-secret-key-must-be-at-least-32-chars-long-for-hs256",
            1,
            "reservas-app"
    );

    @Test
    void generate_yParse_devuelveMismoSubject() {
        // Given
        String subject = "12345678-9";

        // When
        String token = jwtService.generate(subject);
        Claims claims = jwtService.parse(token);

        // Then
        assertThat(claims.getSubject()).isEqualTo(subject);
        assertThat(claims.getIssuer()).isEqualTo("reservas-app");
    }

    @Test
    void parse_conTokenMalformado_lanzaJwtException() {
        // Given
        String tokenInvalido = "esto.no.es.un.jwt";

        // When/Then
        assertThatThrownBy(() -> jwtService.parse(tokenInvalido))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void getExpirationSeconds_retornaSegundosCalculados() {
        // Given / When
        long expiration = jwtService.getExpirationSeconds();

        // Then: 1 hora = 3600 segundos
        assertThat(expiration).isEqualTo(3600L);
    }
}