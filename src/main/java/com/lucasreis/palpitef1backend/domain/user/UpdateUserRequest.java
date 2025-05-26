package com.lucasreis.palpitef1backend.domain.user;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserRequest {
    
    private String name;
    
    @Email(message = "O e-mail deve ser válido")
    private String email;
    
    private UserRole role;
} 