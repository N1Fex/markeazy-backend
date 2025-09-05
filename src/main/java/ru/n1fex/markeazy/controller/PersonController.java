package ru.n1fex.markeazy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.PasswordChangeDto;
import ru.n1fex.markeazy.dto.PersonChangeInfoDto;
import ru.n1fex.markeazy.dto.PersonDto;
import ru.n1fex.markeazy.dto.RegistrationPersonDto;
import ru.n1fex.markeazy.entity.Person;
import ru.n1fex.markeazy.exception.AppError;
import ru.n1fex.markeazy.exception.EmailNotFoundException;
import ru.n1fex.markeazy.exception.WrongOldPasswordException;
import ru.n1fex.markeazy.service.PersonService;

import java.security.Principal;
import java.util.Optional;


@Slf4j
@RestController
public class PersonController {

    private final PersonService personService;

    @Autowired
    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Optional<Person> personOptional = personService.findByEmail(principal.getName());
        return personOptional.map(person ->
                ResponseEntity.ok(new PersonDto(person)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody PersonChangeInfoDto personDto) {
        return ResponseEntity.ok(
                new PersonDto(personService.updatePerson(principal.getName(), personDto))
        );
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(
            Principal principal,
            @RequestBody PasswordChangeDto passwordChangeDto
            ) {
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Новый пароль и подтверждающий его не совпадают!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            Person person = personService.updatePersonPassword(principal.getName(), passwordChangeDto);
            return ResponseEntity.ok(new PersonDto(person));

        } catch (WrongOldPasswordException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверно введен старый пароль!"),
                            HttpStatus.BAD_REQUEST
            );
        } catch (EmailNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь не найден, либо вы не авторизированы!"),
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
