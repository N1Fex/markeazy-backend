package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.n1fex.markeazy.dto.JwtRequest;
import ru.n1fex.markeazy.dto.JwtResponse;
import ru.n1fex.markeazy.dto.RegistrationUserDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.SomethingWentWrongException;
import ru.n1fex.markeazy.mapper.UserMapper;
import ru.n1fex.markeazy.util.JwtTokenUtils;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtTokenUtils jwtTokenUtils;
    private final AuthenticationManager authenticationManager;
    private final UserMapper userMapper;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword())
        );

        User user = userService.findByEmail(authRequest.getEmail()).orElseThrow(SomethingWentWrongException::new);
        String token = jwtTokenUtils.generateToken(user);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> registerUser(@RequestBody RegistrationUserDto userDto) {
        User user = userService.createNewUser(userDto);

        Map<String, Object> map = new HashMap<>();
        map.put("user", userMapper.toUserDto(user));
        map.put("token", jwtTokenUtils.generateToken(user));

        return ResponseEntity.ok(map);
    }

}
