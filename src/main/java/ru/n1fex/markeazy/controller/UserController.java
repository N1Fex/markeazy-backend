package ru.n1fex.markeazy.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.n1fex.markeazy.dto.PasswordChangeDto;
import ru.n1fex.markeazy.dto.UserChangeInfoDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.AppError;
import ru.n1fex.markeazy.exception.EmailNotFoundException;
import ru.n1fex.markeazy.exception.WrongOldPasswordException;
import ru.n1fex.markeazy.mapper.UserMapper;
import ru.n1fex.markeazy.service.UserService;

import java.security.Principal;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        Optional<User> userOptional = userService.findByEmail(principal.getName());
        return userOptional.map(user ->
                ResponseEntity.ok(userMapper.toUserDto(user)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PatchMapping("/profile")
    public ResponseEntity<?> updateProfile(Principal principal, @RequestBody UserChangeInfoDto userDto) {
        return ResponseEntity.ok(
                userMapper.toUserDto(userService.updateUser(principal.getName(), userDto))
        );
    }

    @PatchMapping("/password")
    public ResponseEntity<?> updatePassword(
            Principal principal,
            @RequestBody PasswordChangeDto passwordChangeDto
            ) {
        if (!passwordChangeDto.getNewPassword().equals(passwordChangeDto.getConfirmPassword())) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Новый пароль и подтверждающий его не совпадают!"),
                    HttpStatus.BAD_REQUEST
            );
        }

        try {
            User user = userService.updateUserPassword(principal.getName(), passwordChangeDto);
            return ResponseEntity.ok(userMapper.toUserDto(user));

        } catch (WrongOldPasswordException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Неверно введен старый пароль!"),
                            HttpStatus.BAD_REQUEST
            );
        } catch (EmailNotFoundException e) {
            return new ResponseEntity<>(
                    new AppError(HttpStatus.BAD_REQUEST.value(), "Пользователь не найден, либо вы не авторизированы!"),
                    HttpStatus.BAD_REQUEST
            );
        }

    }
}
