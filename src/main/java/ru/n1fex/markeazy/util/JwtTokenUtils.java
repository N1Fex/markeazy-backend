package ru.n1fex.markeazy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.n1fex.markeazy.entity.Person;
import ru.n1fex.markeazy.entity.Role;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;


    public String generateToken(Person person) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = person.getRoles().stream().map(Role::getName).toList();
        claims.put("roles", roles);
        claims.put("name", person.getName());
        claims.put("registration_date", person.getRegistrationDate());

        Date now = new Date();
        Date expDate = new Date(now.getTime() + lifetime.toMillis());

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(person.getEmail())
                .setIssuedAt(now)
                .setExpiration(expDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

    }

    public String getUsername(String token) {
        return getClaimsFromToken(token).getSubject();
    }

    public List<String> getRoles(String token) {
        return getClaimsFromToken(token).get("roles", List.class);
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
