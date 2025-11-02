package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationUserDto {
    private String email;
    private String name;
    private String password;
    private String confirmPassword;

    private List<CartDto> cartProducts;
}
