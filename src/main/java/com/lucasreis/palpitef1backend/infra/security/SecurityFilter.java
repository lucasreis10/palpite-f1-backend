package com.lucasreis.palpitef1backend.infra.security;

import com.lucasreis.palpitef1backend.domain.user.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            log.debug("Processing request to: {} {}", request.getMethod(), request.getServletPath());

            // Skip filter for public endpoints
            String path = request.getServletPath();
            if (isPublicEndpoint(path, request.getMethod())) {
                log.debug("Skipping security filter for public endpoint: {} {}", request.getMethod(), path);
                filterChain.doFilter(request, response);
                return;
            }

            final String authHeader = request.getHeader("Authorization");
            log.debug("Authorization header present: {}", authHeader != null);

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.debug("No valid authorization header found");
                filterChain.doFilter(request, response);
                return;
            }

            final String jwt = authHeader.substring(7);
            log.debug("Extracted JWT token (length: {})", jwt.length());

            final String userEmail = tokenService.extractUsername(jwt);
            log.debug("Extracted email from token: {}", userEmail);

            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userRepository.findByEmail(userEmail);
                log.debug("Found user details: {}", userDetails != null ? userDetails.getUsername() : "null");
                
                if (userDetails != null && tokenService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication token set in SecurityContext for user: {}", userEmail);
                } else {
                    log.warn("Token validation failed for user: {}", userEmail);
                }
            }
            
        } catch (Exception e) {
            log.error("Error in SecurityFilter: {}", e.getMessage(), e);
            // Não interromper a cadeia de filtros mesmo com erro
            SecurityContextHolder.clearContext();
        }
        
        filterChain.doFilter(request, response);
    }

    private boolean isPublicEndpoint(String path, String method) {
        // Endpoints de autenticação
        if (path.contains("/auth")) {
            return true;
        }
        
        // Endpoint de pilotos (apenas GET)
        if (path.contains("/pilots") && "GET".equals(method)) {
            return true;
        }
        
        // Health checks
        if (path.contains("/health") || path.contains("/actuator")) {
            return true;
        }
        
        return false;
    }
} 