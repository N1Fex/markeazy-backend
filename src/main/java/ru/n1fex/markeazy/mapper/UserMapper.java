package ru.n1fex.markeazy.mapper;

import org.mapstruct.*;
import ru.n1fex.markeazy.dto.RegistrationUserDto;
import ru.n1fex.markeazy.dto.UserChangeInfoDto;
import ru.n1fex.markeazy.dto.UserDto;
import ru.n1fex.markeazy.entity.User;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromChangingDto(UserChangeInfoDto userChangeInfoDto, @MappingTarget User user);

    UserDto toUserDto(User user);
    User toUser(RegistrationUserDto regDto);
}
