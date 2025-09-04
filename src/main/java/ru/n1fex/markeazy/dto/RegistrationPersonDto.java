package ru.n1fex.markeazy.dto;

import lombok.Data;

@Data
public class RegistrationPersonDto {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;
}
