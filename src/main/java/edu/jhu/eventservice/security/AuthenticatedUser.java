package edu.jhu.eventservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;
import java.util.UUID;

/**
 * Helper for retrieving the current authenticated principal.
 * TODO: Replace the UUID parse step once a real UserDetails principal is wired in.
 */
public final class AuthenticatedUser {

    private AuthenticatedUser() {}

    public static Optional<UUID> currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(UUID.fromString(auth.getPrincipal().toString()));
        } catch (IllegalArgumentException ex) {
            return Optional.empty();
        }
    }
}
