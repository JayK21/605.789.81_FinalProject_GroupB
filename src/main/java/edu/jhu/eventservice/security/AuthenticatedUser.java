package edu.jhu.eventservice.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

public final class AuthenticatedUser {

    private AuthenticatedUser() {}

    public static Optional<Integer> currentUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal() == null) {
            return Optional.empty();
        }
        try {
            return Optional.of(Integer.parseInt(auth.getPrincipal().toString()));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }
}
