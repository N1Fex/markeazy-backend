package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.n1fex.markeazy.dto.JwtRequest;
import ru.n1fex.markeazy.dto.RegistrationSellerDto;
import ru.n1fex.markeazy.dto.RegistrationUserDto;
import ru.n1fex.markeazy.exception.WrongPasswordsException;
import ru.n1fex.markeazy.service.AuthService;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        return authService.createAuthToken(authRequest);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto userDto) {

        if (!userDto.getPassword().equals(userDto.getConfirmPassword())) {
            throw new WrongPasswordsException("Пароли не совпадают!");
        }

        return authService.registerUser(userDto);
    }

    @PostMapping("/register/seller")
    public ResponseEntity<?> registerSeller(@RequestBody RegistrationSellerDto sellerDto) {

        if (!sellerDto.getPassword().equals(sellerDto.getConfirmPassword())) {
            throw new WrongPasswordsException("Пароли не совпадают!");
        }

        return authService.registerSeller(sellerDto);
    }

}
