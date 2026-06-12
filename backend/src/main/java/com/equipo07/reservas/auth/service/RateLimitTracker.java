package com.equipo07.reservas.auth.service;

public interface RateLimitTracker {
    void registerFailedAttempt(String ip);
    void registerSuccessfulAttempt(String ip);
}