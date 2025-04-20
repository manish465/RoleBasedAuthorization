package com.manish.user.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.function.Function;

@Service
@Slf4j
public class JWTService {
    public static final long JWT_TOKEN_VALIDITY = 1000 * 60 * 60 * 10;
    private static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    public static String getSubjectFromToken(String token){
        log.info("|| called getSubjectFromToken from JwtService with token : {} ||", token);
        return getClaimFromToken(token, Claims::getSubject);
    }

    public static Set<String> getRolesFromToken(String token) {
        log.info("|| called getRolesFromToken from JwtService with token : {} ||", token);
        Claims claims = getAllClaimsFromToken(token);
        try {
            Object rolesObj = claims.get("roles");
            if (rolesObj instanceof Set) {
                return (Set<String>) rolesObj;
            } else if (rolesObj instanceof java.util.List) {
                return new HashSet<>((List<String>) rolesObj);
            }
            log.warn("Roles claim is neither Set nor List. Type: {}",
                    rolesObj != null ? rolesObj.getClass().getName() : "null");
            return new HashSet<>();
        } catch (Exception e) {
            log.error("Error extracting roles from token: {}", e.getMessage());
            return new HashSet<>();
        }
    }

    public static <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver){
        log.info("|| called getClaimFromToken from JwtService with token : {} and claimsResolver : {} ||", token, claimsResolver.toString());
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public static Claims getAllClaimsFromToken(String token){
        log.info("|| called getAllClaimsFromToken from JwtService with token : {} ||", token);
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private static boolean isTokenExpired(Claims claims){
        log.info("|| called isTokenExpired from JwtService with claims : {} ||", claims.toString());
        long expirationTime = claims.getExpiration().getTime();
        long currentTime = System.currentTimeMillis();

        return currentTime < expirationTime;
    }

    public static String generateToken(String username, Set<String> roles){
        log.info("|| called generateToken from JwtService with username : {} and roles : {} ||", username, roles);
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);
        return doGenerateToken(claims, username);
    }

    private static String doGenerateToken(Map<String, Object> claims, String subject){
        log.info("|| called doGenerateToken from JwtService with claims : {} and subject : {} ||", claims.toString(), subject);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    public static Boolean validateToken(String token){
        log.info("|| called doGenerateToken from JwtService with token : {} ||", token);
        try{
            Claims claims = Jwts
                    .parserBuilder()
                    .setSigningKey(getSignKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return isTokenExpired(claims);
        }catch (JwtException e){
            return false;
        }
    }

    private static Key getSignKey() {
        byte[] keyBytes= Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}