package com.faiyaz.SeekersStop.Service;

import com.faiyaz.SeekersStop.Entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;
    @Value("${jwt.expiration}")
    private long expiration;

    public String generateToken(User user){
        Date now = new Date();
        Date expiry = new Date(now.getTime()+expiration);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        String jwt= Jwts.builder()
                .subject(user.getUsername())
                .claim("role", user.getRole().name())
                .issuedAt(new Date())
                .expiration(expiry)
                .signWith(key)
                .compact();
        return jwt;
    }

    public Claims parseToken(String token){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return claims;
        }


    public String extractUsername(String token){

        String username = parseToken(token).getSubject();
                return username;
    }

    public boolean validateToken(String token, UserDetails userDetails){
        String username = extractUsername(token);
        if((username.equals(userDetails.getUsername()) && !isTokenExpired(token))){
            return true;
        }
        return false;
    }
    public boolean isTokenExpired(String token){
        if(parseToken(token).getExpiration().before(new Date())){
            return true;
        }
        return false;
    }
}
