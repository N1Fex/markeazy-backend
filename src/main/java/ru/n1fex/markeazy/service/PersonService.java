package ru.n1fex.markeazy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.n1fex.markeazy.dto.RegistrationPersonDto;
import ru.n1fex.markeazy.entity.Person;
import ru.n1fex.markeazy.exception.EmailAlreadyExistsException;
import ru.n1fex.markeazy.exception.EmailNotFoundException;
import ru.n1fex.markeazy.repository.PersonRepository;

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
                String.format("Email %s not found", email)
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
            throw new EmailAlreadyExistsException(String.format("Username %s already exists!", personDto.getEmail()));
        }
        Person person = new Person();
        person.setPassword(passwordEncoder.encode(personDto.getPassword()));
        person.setName(personDto.getName());
        person.setEmail(personDto.getEmail());
        person.setRoles(Set.of(roleService.getUserRole()));
        return personRepository.save(person);
    }

}
