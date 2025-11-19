package com.karan.village_milk_app.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.hibernate.sql.ast.tree.expression.Collation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class JwtProvider {

    private SecretKey secretKey;

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
    }

    public String generateToken(org.springframework.security.core.Authentication auth) {
        Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
        String rolesCsv = populateAuthorities(authorities); // will contain ROLE_* values

        Date now = new Date();
        Date expiry = new Date(now.getTime() + JwtConstant.EXPIRATION_MS);

        String jwt = Jwts.builder()
                .setSubject(auth.getName())                 // subject = phone (or username)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .claim("phone", auth.getName())
                .claim("authorities", rolesCsv)
                .signWith(secretKey)
                .compact();

        return jwt;
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String getPhoneFromJwtToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return String.valueOf(claims.get("phone"));
    }

    public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        String authorities = String.valueOf(claims.get("authorities"));
        if (authorities == null || authorities.isBlank()) return Collections.emptyList();

        return Arrays.stream(authorities.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();

        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }

        return String.join(",", auths);
    }

}