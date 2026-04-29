package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationSellerDto {
    private String login;
    private String name;
    private String password;
    private String confirmPassword;
}
