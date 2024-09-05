package com.organiza.api.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
public class JwtUtils {

    public static final String JWT_BEARER = "Bearer";
    public static final String JWT_AUTHORIZATION = "Authorization" ;
    public static final String SECRET_KEY =  "12725533a-329e8fd06b-13e6ab176fg"  ;
    public static final long EXPIRE_DAYS = 0;
    public static final long EXPIRE_HOURS = 0;
    public static final long EXPIRE_MINUTES = 120;

    private JwtUtils(){

    }

    private static Key generateKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start){
        LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES);

        return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String email, String role){
        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        String token = Jwts.builder()
                .setHeaderParam("typ", "jwt")
                .setSubject(email)
                .setIssuedAt(issuedAt)
                .setExpiration(limit)
                .signWith(generateKey(), SignatureAlgorithm.HS256)
                .claim("role", role)
                .compact();

        return new JwtToken(token);
    }

    private static Claims getClaimsFromToken(String token){
        try {
            return Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();


        } catch (JwtException ex){
            log.error(String.format("Token inválido %s", ex.getMessage()));
        }

        return null;
    }

    public static String getEmailFromToken(String token){
        return getClaimsFromToken(token).getSubject();
    }

    public static boolean isTokenValid(String token){
        try {
             Jwts.parser()
                    .setSigningKey(generateKey()).build()
                    .parseClaimsJws(refactorToken(token)).getBody();

            return true;

        } catch (JwtException ex){
            log.error(String.format("Invalid token %s", ex.getMessage()));
        }

        return false;
    }
    private static String refactorToken(String token){
        if(token.contains(JWT_BEARER)) return token.substring(JWT_BEARER.length()).trim();


        return token;
    }
    }





