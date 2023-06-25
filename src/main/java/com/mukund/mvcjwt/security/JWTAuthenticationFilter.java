package com.mukund.mvcjwt.security;

import java.io.IOException;
import java.util.UUID;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mukund.mvcjwt.entity.AuthUser;
import com.mukund.mvcjwt.service.AuthUserDetailsService;
import com.mukund.mvcjwt.service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTService jwtHandler;
    private final AuthUserDetailsService authUserDetailsService;

    public JWTAuthenticationFilter(JWTService jwtHandler, AuthUserDetailsService authUserDetailsService) {
        this.jwtHandler = jwtHandler;
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            String uuid = jwtHandler.getIDFromToken(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                AuthUser user = authUserDetailsService.loadUserByUUID(UUID.fromString(uuid));

                if (jwtHandler.isTokenValid(token, user)) {

                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            user, null, user.getAuthorities());

                    authenticationToken.setDetails(new WebAuthenticationDetailsSource());

                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);

        } else {
            filterChain.doFilter(request, response);
            return;
        }
    }

}