package com.mukund.mvcjwt.service;

import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.mukund.mvcjwt.entity.AuthUser;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JWTService {

    private final Duration validity;
    private final Key secret;

    public JWTService(@Value("${app.jwt.validity}") Duration validity, @Value("${app.jwt.secret}") String secret) {
        this.validity = validity;
        this.secret = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Method to extract username from token
     * 
     * @param token JWT token
     * @return username
     */
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    /**
     * Method to extract ID from token
     * 
     * @param token JWT token
     * @return username
     */
    public String getIDFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    /**
     * Method to extract expiry date from token
     * 
     * @param token JWT token
     * @return Date
     */
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    /**
     * Method to extract claims from token using the strategy given
     * 
     * @param <T>            Claim type
     * @param token          JWT token
     * @param claimsResolver Claim strategy
     * @return claim object
     */
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(getAllClaimsFromToken(token));
    }

    /**
     * Method to extract all claims from token
     * 
     * @param token JWT token
     * @return Claims
     */
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(secret).build().parseClaimsJws(token)
                .getBody();
    }

    /**
     * Method to check if the token is expired or not
     * 
     * @param token JWT token
     * @return true if expired; false if not
     */
    private boolean isTokenExpired(String token) {
        return getExpirationDateFromToken(token).before(new Date());
    }

    /**
     * Method to generate JWT access token. Uses username for claims
     * 
     * @param userDetails User to generate token for
     * @return JWT token
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(
                jwtBuilder -> jwtBuilder.setSubject(userDetails.getUsername()),
                Map.of("type", "access"))
                .compact();
    }

    /**
     * Method to generate JWT refresh token. Uses username for claims
     * 
     * @param userDetails User to generate token for
     * @return JWT token
     */
    public String generateRefreshToken(UserDetails userDetails) {
        return generateToken(
                jwtBuilder -> jwtBuilder.setSubject(userDetails.getUsername()),
                Map.of("type", "refresh"))
                .compact();
    }

    /**
     * Method to generate JWT access token. Uses UUID and username for claims
     * 
     * @param user User to generate token for
     * @return
     */
    public String generateToken(AuthUser user) {
        return generateToken(
                jwtBuilder -> jwtBuilder.setId(user.getId().toString())
                        .setSubject(user.getUsername()),
                Map.of("type", "access"))
                .compact();
    }

    /**
     * Method to generate JWT refresh token. Uses UUID and username for claims
     * 
     * @param user User to generate token for
     * @return
     */
    public String generateRefreshToken(AuthUser user) {
        return generateToken(
                jwtBuilder -> jwtBuilder.setId(user.getId().toString())
                        .setSubject(user.getUsername()),
                Map.of("type", "refresh"))
                .compact();
    }

    /**
     * Method to generate token using default claims and default secret key with
     * HS512 algo
     * 
     * @param jwtBuilder
     * @return
     */
    private JwtBuilder generateToken(UnaryOperator<JwtBuilder> jwtBuilder, Map<String, String> claims) {
        long now = Instant.now().toEpochMilli();
        return jwtBuilder.apply(Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + validity.toMillis()))
                .signWith(secret, SignatureAlgorithm.HS512));
    }

    /**
     * Method to validate token using username and expiry
     * 
     * @param token       JWT token
     * @param userDetails UserDetails object used to get username
     * @return true if token is valid; false if token is not valid
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        return (getUsernameFromToken(token).equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    /**
     * Method to validate token using username , ID and expiry
     * 
     * @param token    JWT token
     * @param AuthUser user object used to get username
     * @return true if token is valid; false if token is not valid
     */
    public boolean isTokenValid(String token, AuthUser user) {
        return (getUsernameFromToken(token).equals(user.getUsername())
                && getIDFromToken(token).equals(user.getId().toString()) && !isTokenExpired(token));
    }

}