package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PersonChangeInfoDto {

    private String name;

    public PersonChangeInfoDto() {
    }
}
