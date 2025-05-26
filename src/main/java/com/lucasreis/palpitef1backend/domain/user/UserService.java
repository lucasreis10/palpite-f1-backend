package com.lucasreis.palpitef1backend.domain.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public List<UserResponse> getAllUsers() {
        log.debug("Buscando todos os usuários");
        return userRepository.findAll()
                .stream()
                .map(UserResponse::fromUser)
                .collect(Collectors.toList());
    }
    
    public UserResponse getUserById(Long id) {
        log.debug("Buscando usuário por ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return UserResponse.fromUser(user);
    }
    
    public UserResponse createUser(CreateUserRequest request) {
        log.debug("Criando novo usuário: {}", request.getEmail());
        
        // Verificar se o email já existe
        if (userRepository.findByEmail(request.getEmail()) != null) {
            throw new RuntimeException("Email já está em uso");
        }
        
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();
        
        User savedUser = userRepository.save(user);
        log.debug("Usuário criado com sucesso: {}", savedUser.getEmail());
        
        return UserResponse.fromUser(savedUser);
    }
    
    public UserResponse updateUser(Long id, UpdateUserRequest request) {
        log.debug("Atualizando usuário ID: {}", id);
        
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        if (request.getName() != null) {
            user.setName(request.getName());
        }
        
        if (request.getEmail() != null) {
            // Verificar se o novo email já está em uso por outro usuário
            User existingUser = (User) userRepository.findByEmail(request.getEmail());
            if (existingUser != null && !existingUser.getId().equals(id)) {
                throw new RuntimeException("Email já está em uso");
            }
            user.setEmail(request.getEmail());
        }
        
        if (request.getRole() != null) {
            user.setRole(request.getRole());
        }
        
        User savedUser = userRepository.save(user);
        log.debug("Usuário atualizado com sucesso: {}", savedUser.getEmail());
        
        return UserResponse.fromUser(savedUser);
    }
    
    public void deleteUser(Long id) {
        log.debug("Deletando usuário ID: {}", id);
        
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        
        userRepository.deleteById(id);
        log.debug("Usuário deletado com sucesso: {}", id);
    }
} 