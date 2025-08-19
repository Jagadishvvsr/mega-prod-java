package com.example.megaservice.security;

import com.example.megaservice.user.UserRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenService tokens;
    private final UserRepository users;

    public JwtAuthFilter(JwtTokenService tokens, UserRepository users) {
        this.tokens = tokens;
        this.users = users;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (auth != null && auth.startsWith("Bearer ")) {
            String jwt = auth.substring(7);
            try {
                String email = tokens.getSubject(jwt);
                String role = tokens.getRole(jwt);
                if (email != null && users.existsByEmail(email)) {
                    var authToken = new UsernamePasswordAuthenticationToken(email, null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + (role == null ? "USER" : role))));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception ignored) { }
        }
        chain.doFilter(request, response);
    }
}
