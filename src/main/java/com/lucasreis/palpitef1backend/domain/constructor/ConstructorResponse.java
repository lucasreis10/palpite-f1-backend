package com.lucasreis.palpitef1backend.domain.constructor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConstructorResponse {
    
    private Long id;
    
    private String constructorId;
    
    private String name;
    
    private String nationality;
    
    private String url;
    
    private Boolean active;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    public static ConstructorResponse fromConstructor(Constructor constructor) {
        return ConstructorResponse.builder()
                .id(constructor.getId())
                .constructorId(constructor.getConstructorId())
                .name(constructor.getName())
                .nationality(constructor.getNationality())
                .url(constructor.getUrl())
                .active(constructor.getActive())
                .createdAt(constructor.getCreatedAt())
                .updatedAt(constructor.getUpdatedAt())
                .build();
    }
} 