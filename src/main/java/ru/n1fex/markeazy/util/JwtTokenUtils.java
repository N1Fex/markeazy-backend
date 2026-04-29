package ru.n1fex.markeazy.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import ru.n1fex.markeazy.entity.AccountType;
import ru.n1fex.markeazy.security.AuthPrincipal;

import java.security.Key;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtTokenUtils {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.lifetime}")
    private Duration lifetime;


    public String generateToken(AuthPrincipal principal) {
        Map<String, Object> claims = new HashMap<>();

        List<String> roles = principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
        claims.put("roles", roles);
        claims.put("name", principal.getName());
        claims.put("accountType", principal.getAccountType().name());
        claims.put("accountId", principal.getId());

        Date now = new Date();
        Date expDate = new Date(now.getTime() + lifetime.toMillis());

        Key key = Keys.hmacShaKeyFor(secret.getBytes());

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(principal.getUsername())
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

    public AccountType getAccountType(String token) {
        return AccountType.valueOf(getClaimsFromToken(token).get("accountType", String.class));
    }

    public Long getAccountId(String token) {
        Number accountId = getClaimsFromToken(token).get("accountId", Number.class);
        return accountId != null ? accountId.longValue() : null;
    }

    public Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
