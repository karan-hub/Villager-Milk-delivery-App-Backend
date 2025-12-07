package com.karan.village_milk_app.Config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karan.village_milk_app.Dto.ApiError;
import com.karan.village_milk_app.Security.JwtAuthenticationFilter;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private JwtAuthenticationFilter jwtAuthenticationFilter ;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity  security) throws Exception {
        security
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .authorizeHttpRequests(
                        auth-> auth
                                .requestMatchers("/api/v1/auth/register").permitAll()
                                .requestMatchers("/api/v1/auth/login").permitAll()
                                .requestMatchers("/api/v1/auth/refresh").permitAll()
                                .requestMatchers("/api/v1/auth/logout").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionConfig->sessionConfig
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .logout(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception->exception
                        .authenticationEntryPoint((request, response, authException) -> {
                            authException.printStackTrace();
                            response.setStatus(402);
                            response.setContentType("application/json");
                            String message = "Unauthorized Access ! "+authException.getMessage();
                            String error = (String) request.getAttribute("error");
                            if (error != null) message=error;

                            var apiError =   ApiError.of(HttpStatus.UNAUTHORIZED.value(), "Unauthorized Access !  ",message ,request.getRequestURI() , true );
                            var mapper = new ObjectMapper();
                            response.getWriter().write(mapper.writeValueAsString(apiError));

                        }))
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class);
        return  security.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return  configuration.getAuthenticationManager();
    }

}
