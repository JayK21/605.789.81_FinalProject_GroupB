package edu.jhu.eventservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Optional;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final String issuer;
    private final long expirationMinutes;

    public JwtService(
            @Value("${app.security.jwt.secret}") String secret,
            @Value("${app.security.jwt.issuer}") String issuer,
            @Value("${app.security.jwt.expiration-minutes}") long expirationMinutes) {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.issuer = issuer;
        this.expirationMinutes = expirationMinutes;
    }

    public String generateToken(String subject) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expirationMinutes * 60);
        return Jwts.builder()
                .issuer(issuer)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(signingKey)
                .compact();
    }

    public Optional<String> parseSubject(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(signingKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return Optional.ofNullable(claims.getSubject());
        } catch (Exception ex) {
            // TODO: Log token validation failures via proper logger.
            return Optional.empty();
        }
    }
}
