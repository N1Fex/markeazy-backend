package ru.n1fex.markeazy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PasswordChangeDto {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public PasswordChangeDto() {}

}
