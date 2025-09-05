package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.PasswordChangeDto;
import ru.n1fex.markeazy.dto.PersonChangeInfoDto;
import ru.n1fex.markeazy.dto.PersonDto;
import ru.n1fex.markeazy.dto.RegistrationPersonDto;
import ru.n1fex.markeazy.entity.Person;
import ru.n1fex.markeazy.exception.EmailAlreadyExistsException;
import ru.n1fex.markeazy.exception.EmailNotFoundException;
import ru.n1fex.markeazy.exception.WrongOldPasswordException;
import ru.n1fex.markeazy.repository.PersonRepository;

import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PersonService implements UserDetailsService {

    private final PersonRepository personRepository;
    private final RoleService roleService;
    private final BCryptPasswordEncoder passwordEncoder;


    public Optional<Person> findByEmail(String email) {
        return personRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws EmailNotFoundException {
        Person user = findByEmail(email).orElseThrow(() -> new EmailNotFoundException(
                String.format("Пользователь с почтой %s не найден!", email)
        ));
        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
    }

    public Person createNewUser(RegistrationPersonDto personDto) throws EmailAlreadyExistsException {
        Optional<Person> existedUser = personRepository.findByEmail(personDto.getEmail());
        if (existedUser.isPresent()) {
            throw new EmailAlreadyExistsException(String.format("Пользователь %s уже существует!", personDto.getEmail()));
        }
        Person person = new Person();
        person.setPassword(passwordEncoder.encode(personDto.getPassword()));
        person.setName(personDto.getName());
        person.setEmail(personDto.getEmail());
        person.setRoles(Set.of(roleService.getUserRole()));
        person.setRegistrationDate(new Date());
        return personRepository.save(person);
    }

    public Person updatePerson(String email, PersonChangeInfoDto personDto) {
        Optional<Person> existedUser = personRepository.findByEmail(email);
        if (existedUser.isPresent()) {
            Person person = existedUser.get();
            person.setName(personDto.getName());
            return personRepository.save(person);
        }
        throw new EmailNotFoundException("Пользователь не найден!");
    }

    public Person updatePersonPassword(String email, PasswordChangeDto passwordChangeDto) throws WrongOldPasswordException {
        Optional<Person> existedUser = personRepository.findByEmail(email);
        if (existedUser.isPresent()) {
            Person person = existedUser.get();
            if (!passwordEncoder.matches(passwordChangeDto.getOldPassword(), person.getPassword())) {
                throw new WrongOldPasswordException("Неверно введен старый пароль!");
            }
            person.setPassword(passwordEncoder.encode(passwordChangeDto.getNewPassword()));
            return personRepository.save(person);
        }
        throw new EmailNotFoundException("Пользователь не найден!");
    }

}
