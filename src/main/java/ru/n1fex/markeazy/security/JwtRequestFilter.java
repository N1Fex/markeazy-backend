package ru.n1fex.markeazy.security;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.n1fex.markeazy.entity.AccountType;
import ru.n1fex.markeazy.util.JwtTokenUtils;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtTokenUtils jwtTokenUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        String username = null;
        String jwtToken = null;
        AccountType accountType = null;
        Long accountId = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwtToken = authHeader.substring(7);

            try {
                username = jwtTokenUtils.getUsername(jwtToken);
                accountType = jwtTokenUtils.getAccountType(jwtToken);
                accountId = jwtTokenUtils.getAccountId(jwtToken);
            } catch (ExpiredJwtException e) {
                log.debug("Token lifetime expired");
            } catch (SignatureException e) {
                log.debug("Invalid token signature");
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            AuthPrincipal principal = new AuthPrincipal(
                    accountId,
                    username,
                    null,
                    null,
                    accountType,
                    jwtTokenUtils.getRoles(jwtToken).stream().map(SimpleGrantedAuthority::new).toList()
            );
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(
                    principal,
                    null,
                    principal.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(token);
        }

        filterChain.doFilter(request, response);
    }
}
