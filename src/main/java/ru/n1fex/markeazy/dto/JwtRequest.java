package ru.n1fex.markeazy.dto;

import lombok.Data;

@Data
public class JwtRequest {
    private String email;
    private String password;
}
