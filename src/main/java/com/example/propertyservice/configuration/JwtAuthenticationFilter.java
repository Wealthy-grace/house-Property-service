//package com.example.propertyservice.configuration;
//
//import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.Jwts;
//import io.jsonwebtoken.io.Decoders;
//import io.jsonwebtoken.security.Keys;
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.security.Key;
//import java.util.Collections;
//import java.util.List;
//
//@Component
//@Slf4j
//public class JwtAuthenticationFilter extends OncePerRequestFilter {
//
//    @Value("${spring.app.jwtSecret}")
//    private String jwtSecret;
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest request,
//                                    HttpServletResponse response,
//                                    FilterChain filterChain) throws ServletException, IOException {
//        log.info("JWT Filter processing request: {} {}", request.getMethod(), request.getRequestURI());
//
//        try {
//            String authHeader = request.getHeader("Authorization");
//            log.info("Authorization header: {}", authHeader != null ? "Present" : "Missing");
//
//            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                log.info("No valid Authorization header found, continuing without authentication");
//                filterChain.doFilter(request, response);
//                return;
//            }
//
//            String jwt = authHeader.substring(7);
//            log.info("Extracted JWT token (first 20 chars): {}...", jwt.substring(0, Math.min(20, jwt.length())));
//
//            // Use Base64 decoding to match User Service
//            Key key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
//            Claims claims = Jwts.parserBuilder()
//                    .setSigningKey(key)
//                    .build()
//                    .parseClaimsJws(jwt)
//                    .getBody();
//
//            String username = claims.getSubject();
//            String role = claims.get("role", String.class);
//
//            log.info("JWT authenticated - Username: {}, Role from JWT: {}", username, role);
//
//            // Create authorities with ROLE_ prefix
//            List<SimpleGrantedAuthority> authorities = Collections.singletonList(
//                    new SimpleGrantedAuthority("ROLE_" + (role != null ? role.toUpperCase() : "STUDENT"))
//            );
//
//            // Log the authorities being set for debugging
//            log.info("Authorities set in SecurityContext: {}", authorities);
//
//            UsernamePasswordAuthenticationToken authentication =
//                    new UsernamePasswordAuthenticationToken(username, null, authorities);
//            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            log.info("Authentication successful - SecurityContext updated for user: {}", username);
//
//        } catch (Exception e) {
//            log.error("JWT authentication failed: {} - {}", e.getClass().getSimpleName(), e.getMessage());
//            log.error("Full stack trace: ", e);
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}