package com.example.megaservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtTokenService {

    private final Key key;
    private final long expiryMs;

    public JwtTokenService(@Value("${app.jwt.secret}") String secret,
                           @Value("${app.jwt.expiryMs:86400000}") long expiryMs) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiryMs = expiryMs;
    }

    public String createToken(String subject, String role) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + expiryMs))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String getSubject(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public String getRole(String token) {
        Object r = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().get("role");
        return r == null ? null : r.toString();
    }
}
