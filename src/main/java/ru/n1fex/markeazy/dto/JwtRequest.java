package ru.n1fex.markeazy.dto;

import lombok.Data;
import ru.n1fex.markeazy.entity.AccountType;

@Data
public class JwtRequest {
    private String email;
    private String login;
    private String password;
    private AccountType accountType;
}
