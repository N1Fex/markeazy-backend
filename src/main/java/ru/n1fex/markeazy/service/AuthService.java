package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.n1fex.markeazy.dto.JwtRequest;
import ru.n1fex.markeazy.dto.JwtResponse;
import ru.n1fex.markeazy.dto.PersonDto;
import ru.n1fex.markeazy.dto.RegistrationPersonDto;
import ru.n1fex.markeazy.entity.Person;
import ru.n1fex.markeazy.exception.AppError;
import ru.n1fex.markeazy.exception.EmailAlreadyExistsException;
import ru.n1fex.markeazy.util.JwtTokenUtils;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final PersonService personService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.UNAUTHORIZED.value(), "Неправильная почта или пароль!"),
                    HttpStatus.UNAUTHORIZED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_GATEWAY.value(), e.getMessage()), HttpStatus.BAD_GATEWAY
            );
        }
        UserDetails userDetails = personService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenUtils.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registerUser(@RequestBody RegistrationPersonDto personDto) {
        if (!personDto.getPassword().equals(personDto.getConfirmPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пароли не совпадают"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Person person = personService.createNewUser(personDto);
            return ResponseEntity.ok(new PersonDto(
                    person.getId(),
                    person.getName(),
                    person.getEmail()
            ));
        } catch (EmailAlreadyExistsException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь с такой почтой уже существует"),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
