package com.lucasreis.palpitef1backend.infra.security;

import com.lucasreis.palpitef1backend.domain.user.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    public String generateToken(User user) {
        return generateToken(new HashMap<>(), user);
    }

    public String generateToken(Map<String, Object> extraClaims, User user) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        } catch (Exception e) {
            log.warn("Erro ao extrair username do token: {}", e.getMessage());
            return null;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        if (claims == null) {
            return null;
        }
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        try {
            if (userDetails == null) {
                log.warn("UserDetails é null para validação de token");
                return false;
            }
            
            final String username = extractUsername(token);
            if (username == null) {
                log.warn("Username extraído do token é null");
                return false;
            }
            
            boolean usernameMatches = username.equals(userDetails.getUsername());
            boolean tokenNotExpired = !isTokenExpired(token);
            
            log.debug("Token validation - Username matches: {}, Token not expired: {}", 
                     usernameMatches, tokenNotExpired);
            
            return usernameMatches && tokenNotExpired;
        } catch (Exception e) {
            log.warn("Erro na validação do token: {}", e.getMessage());
            return false;
        }
    }

    private boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            if (expiration == null) {
                log.warn("Data de expiração do token é null");
                return true;
            }
            return expiration.before(new Date());
        } catch (Exception e) {
            log.warn("Erro ao verificar expiração do token: {}", e.getMessage());
            return true;
        }
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts
                    .parserBuilder()
                    .setSigningKey(getSignInKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            log.warn("Token JWT expirado: {}", e.getMessage());
            throw e; // Re-throw para que o caller possa tratar especificamente
        } catch (UnsupportedJwtException e) {
            log.error("Token JWT não suportado: {}", e.getMessage());
            return null;
        } catch (MalformedJwtException e) {
            log.error("Token JWT malformado: {}", e.getMessage());
            return null;
        } catch (SignatureException e) {
            log.error("Assinatura do token JWT inválida: {}", e.getMessage());
            return null;
        } catch (IllegalArgumentException e) {
            log.error("Token JWT vazio ou inválido: {}", e.getMessage());
            return null;
        } catch (JwtException e) {
            log.error("Erro geral no token JWT: {}", e.getMessage());
            return null;
        } catch (Exception e) {
            log.error("Erro inesperado ao processar token JWT: {}", e.getMessage(), e);
            return null;
        }
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
} 