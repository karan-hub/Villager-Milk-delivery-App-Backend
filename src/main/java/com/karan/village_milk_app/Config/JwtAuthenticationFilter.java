package com.karan.village_milk_app.Config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    public JwtAuthenticationFilter(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader(JwtConstant.JWT_HEADER);

        // If no header or doesn't start with "Bearer ", continue the chain (public endpoints)
        if (header == null || !header.startsWith(JwtConstant.BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(JwtConstant.BEARER_PREFIX.length());

        try {
            // validate + parse
            Claims claims = jwtProvider.getAllClaimsFromToken(token);

            String phone = String.valueOf(claims.get("phone"));
            List<GrantedAuthority> authorities = jwtProvider.getAuthoritiesFromToken(token);

            Authentication authentication = new UsernamePasswordAuthenticationToken(phone, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // continue
            filterChain.doFilter(request, response);

        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            sendUnauthorized(response, "Token expired");
        } catch (io.jsonwebtoken.JwtException | IllegalArgumentException e) {
            sendUnauthorized(response, "Invalid token");
        } catch (Exception e) {
            sendUnauthorized(response, "Authentication error");
        }
    }

    private void sendUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String body = String.format("{\"error\":\"%s\"}", message);
        response.getWriter().write(body);
    }
}
