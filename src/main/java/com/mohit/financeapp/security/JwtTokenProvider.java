package com.mohit.financeapp.security;

import java.security.Key;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.context.annotation.Configuration;

//import org.springframework.security.core.Authentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
@Configuration
public class JwtTokenProvider {


    @Value("${app.jwt-secret}")
    private String SECRET_KEY;
    @Value("${app.jwt-expiration}")
    private Long JWT_EXPIRATION;

    public String generateToken(Authentication authentication){
        String username=authentication.getName();
        Date currentDate=new Date();
        Date expireDate=new Date(currentDate.getTime()+JWT_EXPIRATION);

        return Jwts.builder()
        .setSubject(username)
        .setIssuedAt(currentDate)
        .setExpiration(expireDate)
        .signWith(key(),SignatureAlgorithm.HS256)
        .compact();

    }
    private Key key(){
        byte []keyBytes=Decoders.BASE64.decode(SECRET_KEY);
     
        return Keys.hmacShaKeyFor(keyBytes);
    }
    public String getUsername(String token){
        Claims claims=Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();

        return claims.getSubject();
    }
    public boolean validateToken(String token){
        Claims claims=Jwts.parserBuilder()
        .setSigningKey(key())
        .build()
        .parseClaimsJws(token)
        .getBody();

        return true;
    }
}
