package ru.n1fex.markeazy.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.n1fex.markeazy.entity.AccountType;
import ru.n1fex.markeazy.entity.Role;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.entity.User;

import java.util.Collection;
import java.util.List;

@Getter
public class AuthPrincipal implements UserDetails {

    private final Long id;
    private final String username;
    private final String password;
    private final String name;
    private final AccountType accountType;
    private final List<? extends GrantedAuthority> authorities;

    public AuthPrincipal(
            Long id,
            String username,
            String password,
            String name,
            AccountType accountType,
            List<? extends GrantedAuthority> authorities
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.accountType = accountType;
        this.authorities = authorities;
    }

    public static AuthPrincipal fromUser(User user) {
        return new AuthPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getName(),
                AccountType.USER,
                user.getRoles().stream()
                        .map(Role::getName)
                        .map(org.springframework.security.core.authority.SimpleGrantedAuthority::new)
                        .toList()
        );
    }

    public static AuthPrincipal fromSeller(Seller seller) {
        return new AuthPrincipal(
                seller.getId(),
                seller.getLogin(),
                seller.getPassword(),
                seller.getName(),
                AccountType.SELLER,
                seller.getRoles().stream()
                        .map(Role::getName)
                        .map(SimpleGrantedAuthority::new)
                        .toList()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
