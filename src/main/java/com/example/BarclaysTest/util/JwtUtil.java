package com.example.BarclaysTest.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public String generateToken(String userId) {
        return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateTokenAndGetUserId(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    //can move to util and be a static class if u dont want to instantiate it
    //token will look like bearer
//    "Bearer " is part of the OAuth 2.0 standard (RFC 6750).
//    It tells the server how to interpret the token (as a Bearer token).
//    Without it, the server has no standard clue what kind of authentication is being used.
    public String getTokenFromHeader(String authHeader){
        return (authHeader != null && authHeader.startsWith("Bearer ")) ? authHeader.substring(7) : null ;
    }


    //if the change works then remove this and just get user id in endpoints
    public static String getAuthenticatedUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof String) {
            return (String) auth.getPrincipal();
        }
        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Access token is missing or invalid");
    }
}
