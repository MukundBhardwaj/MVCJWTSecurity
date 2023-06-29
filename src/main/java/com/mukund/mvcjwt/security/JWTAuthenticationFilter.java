package com.mukund.mvcjwt.security;

import java.io.IOException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.mukund.mvcjwt.model.AuthUserDetails;
import com.mukund.mvcjwt.service.JWTService;
import com.mukund.mvcjwt.service.implementation.AuthUserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JWTAuthenticationFilter.class);
    private final JWTService jwtHandler;
    private final AuthUserDetailsServiceImpl authUserDetailsService;

    public JWTAuthenticationFilter(JWTService jwtHandler, AuthUserDetailsServiceImpl authUserDetailsService) {
        this.jwtHandler = jwtHandler;
        this.authUserDetailsService = authUserDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7);

            try {

                String uuid = jwtHandler.getIDFromToken(token);

                if (SecurityContextHolder.getContext().getAuthentication() == null) {

                    AuthUserDetails user = authUserDetailsService.loadUserByID(UUID.fromString(uuid));

                    if (jwtHandler.isTokenValid(token, user)) {

                        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                                user, null, user.getAuthorities());

                        authenticationToken.setDetails(new WebAuthenticationDetailsSource());

                        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    }
                }
            } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException
                    | IllegalArgumentException e) {
                LOGGER.warn("Exception occurred in JWTAuthenticationFilter :", e);
            }
        }
        filterChain.doFilter(request, response);
    }

}