package com.karan.village_milk_app.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karan.village_milk_app.Dto.ApiError;
import com.karan.village_milk_app.Security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter ;
    private  final  SecurityEndpoints securityEndpoints;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity  security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth-> auth
//                                public's URLs
                                .requestMatchers(securityEndpoints.PUBLIC_ENDPOINTS.toArray(String[]::new)).permitAll()
//                               Admin URLs
                                .requestMatchers(securityEndpoints.ADMIN_ENDPOINTS.toArray(String[]::new)).hasRole("ADMIN")

//                                User
                                .requestMatchers(securityEndpoints.USER_ENDPOINTS.toArray(String[]::new)).hasRole("USER")
                                // Others
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionConfig->sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            authException.printStackTrace();
                            response.setStatus(HttpStatus.UNAUTHORIZED.value());
                            response.setContentType("application/json");
                            String message = "Unauthorized Access ! "+authException.getMessage();
                            String error = (String) request.getAttribute("error");
                            if (error != null) message=error;

                            var apiError =   ApiError.of(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Access !  ",message ,request.getRequestURI() , true );
                            var mapper = new ObjectMapper();
                            response.getWriter().write(mapper.writeValueAsString(apiError));

                        })
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            response.setStatus(HttpStatus.FORBIDDEN.value());
                            response.setContentType("application/json");
                            var errorResponse = ApiError.of(
                                    HttpStatus.FORBIDDEN.value(),
                                    "Forbidden",
                                    "You don't have permission for this operation",
                                    request.getRequestURI(),
                                    true
                            );

                            new ObjectMapper().writeValue(response.getWriter(), errorResponse);
                        }))
                )

                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);
        return  security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(List.of("http://localhost:3000", "https://yourdomain.com")); // Configure allowed origins
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        config.setAllowedHeaders(List.of(HttpHeaders.AUTHORIZATION, HttpHeaders.CONTENT_TYPE));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return  configuration.getAuthenticationManager();
    }

}
