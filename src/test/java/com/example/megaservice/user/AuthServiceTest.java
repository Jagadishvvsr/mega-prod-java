package com.example.megaservice.user;

import com.example.megaservice.security.JwtTokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock UserRepository users;
    @Mock PasswordEncoder encoder;
    @Mock JwtTokenService tokens;
    @InjectMocks AuthService auth;

    @Test
    void signup_encodesPassword_and_returnsToken() {
        when(users.existsByEmail("a@b.com")).thenReturn(false);
        when(encoder.encode("pw")).thenReturn("HASH");
        when(tokens.createToken(eq("a@b.com"), any())).thenReturn("TOKEN");
        String t = auth.signup("a@b.com", "pw");
        assertEquals("TOKEN", t);
        verify(users).save(any());
    }

    @Test
    void login_checks_password_and_returnsToken() {
        User u = new User(); u.setEmail("a@b.com"); u.setPasswordHash("HASH");
        when(users.findByEmail("a@b.com")).thenReturn(Optional.of(u));
        when(encoder.matches("pw", "HASH")).thenReturn(true);
        when(tokens.createToken(eq("a@b.com"), any())).thenReturn("T2");
        assertEquals("T2", auth.login("a@b.com", "pw"));
    }
}
