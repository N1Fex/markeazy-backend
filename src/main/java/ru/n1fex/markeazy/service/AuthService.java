package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.n1fex.markeazy.dto.*;
import ru.n1fex.markeazy.entity.AccountType;
import ru.n1fex.markeazy.entity.Seller;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.mapper.UserMapper;
import ru.n1fex.markeazy.security.AuthPrincipal;
import ru.n1fex.markeazy.util.JwtTokenUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserMapper userMapper;
    private final UserService userService;
    private final SellerService sellerService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        String identifier = resolveIdentifier(authRequest);
        log.info("Creating auth token for user {}", authRequest);
        String principalValue = buildAuthenticationPrincipal(identifier, authRequest.getAccountType());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(principalValue, authRequest.getPassword())
        );
        AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();

        String token = jwtTokenUtils.generateToken(authPrincipal);
        return ResponseEntity.ok(new JwtResponse(token, authPrincipal.getAccountType()));
    }

    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto userDto) {
        User user = userService.createNewUser(userDto);
        AuthPrincipal principal = AuthPrincipal.fromUser(user);

        Map<String, Object> map = new HashMap<>();
        map.put("user", userMapper.toUserDto(user));
        map.put("token", jwtTokenUtils.generateToken(principal));
        map.put("accountType", principal.getAccountType());

        return ResponseEntity.ok(map);
    }

    public ResponseEntity<?> registerSeller(@RequestBody RegistrationSellerDto sellerDto) {
        Seller seller = sellerService.createNewSeller(sellerDto);
        AuthPrincipal principal = AuthPrincipal.fromSeller(seller);

        Map<String, Object> map = new HashMap<>();
        map.put("seller", new SellerDto(
                seller.getId(),
                seller.getLogin(),
                seller.getName(),
                seller.getRegistrationDate()
        ));
        map.put("token", jwtTokenUtils.generateToken(principal));
        map.put("accountType", principal.getAccountType());

        return ResponseEntity.ok(map);
    }

    private String resolveIdentifier(JwtRequest authRequest) {
        if (authRequest.getLogin() != null && !authRequest.getLogin().isBlank()) {
            return authRequest.getLogin();
        }

        return authRequest.getEmail();
    }

    private String buildAuthenticationPrincipal(String identifier, AccountType accountType) {
        if (identifier == null || identifier.isBlank()) {
            throw new IllegalArgumentException("Не передан логин или email");
        }

        if (accountType == null) {
            return identifier;
        }

        return accountType.name() + ":" + identifier;
    }
}
