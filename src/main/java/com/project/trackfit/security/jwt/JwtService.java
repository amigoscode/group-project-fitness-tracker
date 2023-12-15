package com.project.trackfit.security.jwt;


import com.project.trackfit.core.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


@Service
public class JwtService {
    final String SECRET_KEY="6E5A7234753778214125442A472D4B614E645267556B58703273357638792F42";

    public String generateToken(String email, Role role){
        Map<String,Object> claims = new HashMap<>();
        claims.put("role", role.name());
        return createToken(claims, email);
    }

    private String createToken( Map<String,Object> claims, String subject) {
        Key key = getSignInKey();
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+
                        1000 * 60 * 60 * 10))
                .signWith(key)
                .compact();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims= extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getSignInKey(){
        byte [] keyBytes= Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserType(String token) {
       return extractAllClaims(token).get("role").toString();
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Boolean validateToken(String token,UserDetails userDetails){
        final String username=extractUsername(token);
        return (
                username.equals(userDetails.getUsername())&&!isTokenExpired((token))
                );
    }
}