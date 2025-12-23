package com.karan.village_milk_app.Security;

import com.karan.village_milk_app.Repositories.UserRepository;
import com.karan.village_milk_app.healpers.UserHelper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final  JwtService jwtService ;
    private final UserRepository userRepository ;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        if (header!=null && header.startsWith("Bearer ")){
            String token = header.substring(7);
            try{
                if (!jwtService.isAccessToken(token)){
                    filterChain.doFilter(request,response);
                    return;
                }
                Jws<Claims> claimsJws = jwtService.parse(token);
                Claims payload = claimsJws.getPayload();
                String userId = payload.getSubject();
                UUID userUuid = UserHelper.parseUUID(userId);
                userRepository.findById(userUuid)
                        .ifPresent(user -> {
                            if (user.isEnabled()){
                                 GrantedAuthority authorities =
                                         user.getRole() == null ?
                                                 new SimpleGrantedAuthority("")
                                                 : new SimpleGrantedAuthority(user.getRole().name());

//                                GrantedAuthority authorities = new SimpleGrantedAuthority(user.getRole().name());

                                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                         user,
                                        null,
                                        List.of(authorities)
                                );
                                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                                if ( SecurityContextHolder.getContext().getAuthentication()==null)
                                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                            }
                        });

            } catch (ExpiredJwtException e){
                request.setAttribute("error","Token Expired");
            }
            catch (Exception  e ){
                request.setAttribute("error","Invalid Token");
            }
        }
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            System.out.println("AUTH PRINCIPAL = " + auth.getName());
            System.out.println("AUTH AUTHORITIES = " + auth.getAuthorities());
        } else {
            System.out.println("NO AUTH IN CONTEXT");
        }


        filterChain.doFilter(request,response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return  request.getRequestURI().startsWith("/api/v1/auth");
    }
}
