package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.PasswordChangeDto;
import ru.n1fex.markeazy.dto.UserChangeInfoDto;
import ru.n1fex.markeazy.dto.RegistrationUserDto;
import ru.n1fex.markeazy.entity.User;
import ru.n1fex.markeazy.exception.EmailAlreadyExistsException;
import ru.n1fex.markeazy.exception.EmailNotFoundException;
import ru.n1fex.markeazy.exception.WrongOldPasswordException;
import ru.n1fex.markeazy.mapper.UserMapper;
import ru.n1fex.markeazy.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserMapper userMapper;

    private final RoleService roleService;
    private final CartService cartService;

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder;


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        User user = findByEmail(email).orElseThrow(() -> new EmailNotFoundException(
                String.format("Пользователь с почтой %s не найден!", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    public User createNewUser(RegistrationUserDto userDto) throws EmailAlreadyExistsException {
        Optional<User> existedUser = userRepository.findByEmail(userDto.getEmail());
        if (existedUser.isPresent()) {
            throw new EmailAlreadyExistsException(String.format("Пользователь %s уже существует!", userDto.getEmail()));
        }
        User user = userMapper.toUser(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(roleService.getUserRole()));
        user.setRegistrationDate(new Date());

        User savedUser = userRepository.save(user);
        cartService.addProductsToCart(savedUser, userDto.getCartProducts() != null ? userDto.getCartProducts() : List.of());

        return savedUser;
    }

    public User updateUser(String email, UserChangeInfoDto userDto) {
        Optional<User> existedUser = userRepository.findByEmail(email);
        if (existedUser.isPresent()) {
            User user = existedUser.get();
            user.setName(userDto.getName());
            return userRepository.save(user);
        }
        throw new EmailNotFoundException("Пользователь не найден!");
    }

    public User updateUserPassword(String email, PasswordChangeDto passwordChangeDto) throws WrongOldPasswordException {
        Optional<User> existedUser = userRepository.findByEmail(email);
        if (existedUser.isPresent()) {
            User user = existedUser.get();
            if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), user.getPassword())) {
                throw new WrongOldPasswordException("Неверно введен старый пароль!");
            }
            user.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            return userRepository.save(user);
        }
        throw new EmailNotFoundException("Пользователь не найден!");
    }

}
