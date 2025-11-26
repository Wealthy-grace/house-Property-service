//package com.example.propertyservice.configuration;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
//    private final JwtAuthenticationFilter jwtAuthFilter;
//
//    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter) {
//        this.jwtAuthFilter = jwtAuthFilter;
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                )
//                .authorizeHttpRequests(authz -> authz
//                        // Public endpoints - anyone can view
//                        .requestMatchers("/actuator/**").permitAll()
//                        .requestMatchers("/api/v1/properties/search/**").permitAll()
//                        .requestMatchers("/api/v1/properties/available").permitAll()
//                        .requestMatchers("/api/v1/properties/{id}").permitAll()
//                        .requestMatchers("/api/v1/properties").permitAll()
//
//                        // Protected endpoints - require authentication
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//}


// TODO
package com.example.propertyservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Spring Security Configuration for Property Service
 *
 * Integrates with Keycloak for authentication and authorization
 *
 * Security Model:
 * - Public endpoints: View properties, search properties
 * - ADMIN + PROPERTY_MANAGER: Create, update, delete properties
 * - All authenticated users: Can view their own bookings/appointments
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                .authorizeHttpRequests(authz -> authz
                        // Public endpoints - anyone can view properties
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers("/api/v1/properties/search/**").permitAll()
                        .requestMatchers("/api/v1/properties/available").permitAll()
                        .requestMatchers("/api/v1/properties/{id}").permitAll()
                        .requestMatchers("/api/v1/properties").permitAll()

                        // Protected endpoints - require authentication
                        // Specific roles are enforced via @PreAuthorize in controllers
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        )
                );

        return http.build();
    }

    /**
     * Custom JWT Authentication Converter
     * Extracts roles from Keycloak's realm_access claim
     */
    @Bean
    public Converter<Jwt, AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());

        // Use preferred_username as the principal name (Keycloak default)
        converter.setPrincipalClaimName("preferred_username");

        return converter;
    }

    /**
     * Extracts authorities/roles from Keycloak JWT token
     *
     * FIXED: No duplicate ROLE_ prefixes!
     * Maps Keycloak roles to Spring Security authorities correctly
     */
    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return jwt -> {
            Collection<GrantedAuthority> authorities = new ArrayList<>();

            // Extract roles from realm_access claim
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null && realmAccess.containsKey("roles")) {
                @SuppressWarnings("unchecked")
                List<String> roles = (List<String>) realmAccess.get("roles");

                // Map each role to an authority with correct prefix handling
                authorities = roles.stream()
                        .map(this::mapRoleToAuthority)
                        .collect(Collectors.toList());
            }

            return authorities;
        };
    }

    /**
     * Map a Keycloak role to a Spring Security GrantedAuthority
     *
     * Prevents duplicate ROLE_ prefixes:
     * - "ROLE_ADMIN" → "ROLE_ADMIN" (not "ROLE_ROLE_ADMIN")
     * - "ROLE_PROPERTY_MANAGER" → "ROLE_PROPERTY_MANAGER"
     * - "default-roles-friendly-housing" → "default-roles-friendly-housing" (no prefix)
     * - "offline_access" → "offline_access" (no prefix)
     * - "ADMIN" → "ROLE_ADMIN" (adds prefix)
     * - "PROPERTY_MANAGER" → "ROLE_PROPERTY_MANAGER" (adds prefix)
     */
    private GrantedAuthority mapRoleToAuthority(String role) {
        // If role already starts with "ROLE_", use it as-is
        if (role.startsWith("ROLE_")) {
            return new SimpleGrantedAuthority(role);
        }

        // Standard Keycloak internal roles - use as-is without ROLE_ prefix
        if (role.startsWith("default-roles-") ||
                role.equals("offline_access") ||
                role.equals("uma_authorization")) {
            return new SimpleGrantedAuthority(role);
        }

        // Custom application roles - add ROLE_ prefix
        return new SimpleGrantedAuthority("ROLE_" + role);
    }
}










