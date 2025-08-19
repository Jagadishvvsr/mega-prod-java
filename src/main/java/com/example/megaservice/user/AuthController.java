package com.example.megaservice.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService auth;

    public AuthController(AuthService auth) { this.auth = auth; }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse signup(@RequestBody AuthRequest req) {
        return new AuthResponse(auth.signup(req.email(), req.password()));
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest req) {
        return new AuthResponse(auth.login(req.email(), req.password()));
    }
}
