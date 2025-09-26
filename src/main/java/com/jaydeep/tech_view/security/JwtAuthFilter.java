package com.jaydeep.tech_view.security;



import com.jaydeep.tech_view.entity.User;
import com.jaydeep.tech_view.service.UserService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        try {
            final String authHeader = request.getHeader("Authorization");

            // Step 1: Check header presence and format
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            // Step 2: Extract token safely
            String[] parts = authHeader.split(" ");
            if (parts.length != 2) {
                filterChain.doFilter(request, response);
                return;
            }
            String token = parts[1];

            // Step 3: Validate token type (must be access token)
            if (!jwtService.isAccessToken(token)) {
                throw new JwtException("Invalid token type. Expected access token.");
            }

            // Step 4: Extract user ID
            Long userId = jwtService.getUserIdFromToken(token);

            // Step 5: Skip if already authenticated
            if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                User user = userService.getUserById(userId);
                if (user == null) {
                    throw new JwtException("User not found with ID: " + userId);
                }

                // Step 6: Build Authentication
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }

            // Step 7: Continue filter chain
            filterChain.doFilter(request, response);

        }
//        need the below as spring web context cant catch and throw the
//        spring security exceptions so we will catch and put in the handler exceptions resolver
        catch (JwtException ex) {
            // Step 8: Delegate to global exception handler
            handlerExceptionResolver.resolveException(request, response, null, ex);
        }
    }
}
