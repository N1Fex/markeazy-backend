package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.n1fex.markeazy.entity.Person;

import java.util.Date;

@Data
@AllArgsConstructor
public class PersonDto {

    private Long id;
    private String name;
    private String email;
    private Date registrationDate;

    public PersonDto(Person person) {
        this.id = person.getId();
        this.name = person.getName();
        this.email = person.getEmail();
        this.registrationDate = person.getRegistrationDate();
    }

    public PersonDto() {}

}