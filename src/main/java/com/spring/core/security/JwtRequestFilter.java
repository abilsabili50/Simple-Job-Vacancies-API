package com.spring.core.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.core.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final ObjectMapper objectMapper;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final HashSet<String> withoutJwtUrls = new HashSet<>(
            Arrays.asList(
                    "/api/v1/auth/login",
                    "/api/v1/auth/register",
                    "/api/v1/vacancies/dashboard",
                    "api/v1/ping"
            )
    );
    private final HashSet<String> swaggerUrls = new HashSet<>(
            Arrays.asList(
                    "/api/v1/swagger-ui/index.html",
                    "/api/v1/swagger-resources",
                    "/api/v1/v2/api-docs",
                    "/api/v1/webjars",
                    "/api/v1.0/swaggerfox.js",
                    "/api/v1.0/swagger-ui",
                    "/api/v1.0/v3/api-docs"
            )
    );

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            if(this.withoutJwtUrls.contains(request.getRequestURI())
                    || this.swaggerUrls.stream().anyMatch(s -> request.getRequestURI().contains(s))){
                filterChain.doFilter(request, response);
                return;
            }

            Map<String, Object> errorResponse = new LinkedHashMap<>();
            errorResponse.put("status", "UNAUTHORIZED");
            errorResponse.put("message", "invalid authorization");
            errorResponse.put("data", null);

            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);

            this.objectMapper.writeValue(response.getWriter(), errorResponse);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            final String email = this.jwtUtil.extractUsername(jwt);
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if(email != null && authentication == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if(this.jwtUtil.validateToken(jwt, userDetails)){
                    UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    usernamePasswordAuthenticationToken
                            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
                }
            }

            filterChain.doFilter(request, response);
        }catch (Exception e){
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
