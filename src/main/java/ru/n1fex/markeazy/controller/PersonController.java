package ru.n1fex.markeazy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.n1fex.markeazy.dto.PersonDto;
import ru.n1fex.markeazy.entity.Person;
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

}
