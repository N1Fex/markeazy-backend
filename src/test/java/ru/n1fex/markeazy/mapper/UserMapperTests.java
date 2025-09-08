package ru.n1fex.markeazy.mapper;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.n1fex.markeazy.MarkeazyApplication;
import ru.n1fex.markeazy.dto.RegistrationUserDto;
import ru.n1fex.markeazy.dto.UserChangeInfoDto;
import ru.n1fex.markeazy.dto.UserDto;
import ru.n1fex.markeazy.entity.Role;
import ru.n1fex.markeazy.entity.User;

import java.util.GregorianCalendar;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = MarkeazyApplication.class)
class UserMapperTests {

    UserMapper mapper = new UserMapperImpl();

    private Role prepareRoleUser() {
        Role role = new Role();
        role.setName("ROLE_USER");
        role.setId(1);
        return role;
    }

    private User preparePerson() {
        User user = new User();
        user.setName("Danny");
        user.setEmail("danny@gmail.com");
        user.setRegistrationDate(new GregorianCalendar(2004, 6, 14).getTime());
        user.setId(14L);
        user.setPassword("password");

        Role role = prepareRoleUser();
        user.setRoles(Set.of(role));

        return user;
    }

    @Test
    public void testPersonToDto() {

        User user =preparePerson();
        UserDto userDto = mapper.toUserDto(user);

        assertNotNull(userDto);

        assertAll(
                () -> assertEquals(user.getId(), userDto.getId()),
                () -> assertEquals(user.getName(), userDto.getName()),
                () -> assertEquals(user.getRegistrationDate(), userDto.getRegistrationDate()),
                () -> assertEquals(user.getEmail(), userDto.getEmail())
        );
    }

    @Test
    public void testUpdatingProfileInfo() {
        User user = preparePerson();

        UserChangeInfoDto changeDto = new UserChangeInfoDto();
        changeDto.setName("Anton");

        mapper.updateUserFromChangingDto(changeDto, user);

        assertNotNull(user);

        User source = preparePerson();

        assertAll(
                () -> assertEquals(user.getName(), changeDto.getName()),

                () -> assertEquals(user.getRoles(), source.getRoles()),
                () -> assertEquals(user.getPassword(), source.getPassword()),
                () -> assertEquals(user.getId(), source.getId()),
                () -> assertEquals(user.getRegistrationDate(), source.getRegistrationDate()),
                () -> assertEquals(user.getEmail(), source.getEmail()),

                () -> assertNotEquals(user.getName(), source.getName())
        );

    }

    @Test
    public void testRegistrationDtoToUser() {
        RegistrationUserDto regDto = new RegistrationUserDto();
        regDto.setName("Anton");
        regDto.setEmail("anton@gmail.com");
        regDto.setPassword("password");
        regDto.setConfirmPassword("password");

        User user = mapper.toUser(regDto);
        assertNotNull(user);

        assertAll(
                () -> assertEquals(user.getName(), regDto.getName()),
                () -> assertEquals(user.getEmail(), regDto.getEmail()),
                () -> assertEquals(user.getPassword(), regDto.getPassword()),

                () -> assertNull(user.getRegistrationDate()),
                () -> assertNull(user.getRoles()),
                () -> assertNull(user.getId())
        );
    }

}
