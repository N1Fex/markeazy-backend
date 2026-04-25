package ru.n1fex.markeazy.dto;

import lombok.Data;
import ru.n1fex.markeazy.entity.AccountType;

@Data
public class JwtResponse {
    private String token;
    private AccountType accountType;

    public JwtResponse(String token, AccountType accountType) {
        this.token = token;
        this.accountType = accountType;
    }
}

