package com.equipo07.reservas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final int maxAttempts;
    private final long windowMillis;
    private final long lockoutMillis;

    private final ConcurrentHashMap<String, AttemptRecord> attemptsByIp = new ConcurrentHashMap<>();

    public RateLimitFilter(@Value("${ratelimit.max-attempts}") int maxAttempts,
                           @Value("${ratelimit.window-seconds}") long windowSeconds,
                           @Value("${ratelimit.lockout-minutes}") long lockoutMinutes) {
        this.maxAttempts = maxAttempts;
        this.windowMillis = windowSeconds * 1000L;
        this.lockoutMillis = lockoutMinutes * 60 * 1000L;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !(request.getRequestURI().startsWith("/api/auth/login")
                || request.getRequestURI().startsWith("/api/auth/register"));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String ip = request.getRemoteAddr();
        long now = Instant.now().toEpochMilli();

        AttemptRecord record = attemptsByIp.compute(ip, (k, v) -> {
            if (v == null) return new AttemptRecord(now, 0, 0);
            if (now - v.firstAttemptAt > windowMillis && v.lockedUntilAt < now) {
                return new AttemptRecord(now, 0, 0);
            }
            return v;
        });

        if (record.lockedUntilAt > now) {
            writeTooManyRequests(response);
            return;
        }

        chain.doFilter(request, response);
    }

    public void registerFailedAttempt(String ip) {
        long now = Instant.now().toEpochMilli();
        attemptsByIp.compute(ip, (k, v) -> {
            if (v == null) return new AttemptRecord(now, 1, 0);
            int newCount = v.failedCount + 1;
            long lockedUntil = (newCount >= maxAttempts) ? now + lockoutMillis : v.lockedUntilAt;
            return new AttemptRecord(v.firstAttemptAt, newCount, lockedUntil);
        });
    }

    public void registerSuccessfulAttempt(String ip) {
        attemptsByIp.remove(ip);
    }

    private void writeTooManyRequests(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(
                "{\"timestamp\":\"" + Instant.now() + "\",\"status\":429,\"error\":\"Too Many Requests\","
                        + "\"message\":\"Demasiados intentos. Intente nuevamente más tarde.\"}");
    }

    private record AttemptRecord(long firstAttemptAt, int failedCount, long lockedUntilAt) {}
}