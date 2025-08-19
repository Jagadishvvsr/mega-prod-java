package com.example.megaservice.user;

import com.example.megaservice.security.JwtTokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtTokenService tokens;

    public AuthService(UserRepository userRepository, PasswordEncoder encoder, JwtTokenService tokens) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.tokens = tokens;
    }

    public String signup(String email, String password) {
        if (userRepository.existsByEmail(email)) throw new IllegalStateException("email_taken");
        User u = new User();
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setRole(User.Role.USER);
        userRepository.save(u);
        return tokens.createToken(email, u.getRole().name());
    }

    public String login(String email, String password) {
        User u = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("invalid_credentials"));
        if (!encoder.matches(password, u.getPasswordHash())) throw new IllegalArgumentException("invalid_credentials");
        return tokens.createToken(email, u.getRole().name());
    }
}
