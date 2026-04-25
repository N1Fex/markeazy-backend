package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.n1fex.markeazy.entity.AccountType;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.repository.SellerRepository;
import ru.n1fex.markeazy.repository.UserRepository;
import ru.n1fex.markeazy.security.AuthPrincipal;

@Service
@RequiredArgsConstructor
public class MarketplaceUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final SellerRepository sellerRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ParsedPrincipal parsedPrincipal = parsePrincipal(username);

        if (parsedPrincipal.accountType() == AccountType.USER) {
            return loadUserPrincipal(parsedPrincipal.identifier());
        }

        if (parsedPrincipal.accountType() == AccountType.SELLER) {
            return loadSellerPrincipal(parsedPrincipal.identifier());
        }

        return userRepository.findByEmail(parsedPrincipal.identifier())
                .<UserDetails>map(AuthPrincipal::fromUser)
                .or(() -> sellerRepository.findByLogin(parsedPrincipal.identifier()).map(AuthPrincipal::fromSeller))
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Пользователь или продавец с таким логином не найден"
                ));
    }

    private AuthPrincipal loadUserPrincipal(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Пользователь с почтой %s не найден", email)
                ));
        return AuthPrincipal.fromUser(user);
    }

    private AuthPrincipal loadSellerPrincipal(String login) {
        Seller seller = sellerRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException(
                        String.format("Продавец с логином %s не найден", login)
                ));
        return AuthPrincipal.fromSeller(seller);
    }

    private ParsedPrincipal parsePrincipal(String username) {
        for (AccountType type : AccountType.values()) {
            String prefix = type.name() + ":";
            if (username.startsWith(prefix)) {
                return new ParsedPrincipal(type, username.substring(prefix.length()));
            }
        }

        return new ParsedPrincipal(null, username);
    }

    private record ParsedPrincipal(AccountType accountType, String identifier) {
    }
}
