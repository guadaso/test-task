package com.krainet.api.users.mapper;

import com.krainet.api.users.dto.UserDto;
import com.krainet.api.users.entity.Users;
import org.mapstruct.*;

@Mapper(componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    @Mapping(target = "id", source = "id")
    UserDto toDto(Users user);

    @Mapping(target = "id", source = "id")
    Users toEntity(UserDto userDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "role", ignore = true)
    void updateEntity(UserDto userDto, @MappingTarget Users user);
}