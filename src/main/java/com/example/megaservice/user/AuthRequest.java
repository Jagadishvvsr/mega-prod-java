// src/main/java/com/example/megaservice/user/AuthRequest.java
package com.example.megaservice.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthRequest(
    @Email String email,
    @NotBlank String password
) {}
