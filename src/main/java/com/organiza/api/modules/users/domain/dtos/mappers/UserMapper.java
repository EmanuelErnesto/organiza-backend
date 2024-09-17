package com.organiza.api.modules.users.domain.dtos.mappers;


import com.organiza.api.modules.users.domain.dtos.CreateUserDto;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {

    public static UserModel mappingToUser(CreateUserDto createUserDto) {
        return new ModelMapper().map(createUserDto, UserModel.class);
    }

    public static UserResponseDto mappingToUserResponse(UserModel user) {
        String role = user.getRole().name().substring("ROLE_".length());
        PropertyMap<UserModel, UserResponseDto> props = new PropertyMap<UserModel, UserResponseDto>() {
            @Override
            protected void configure() {
                map().setRole(role);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(user, UserResponseDto.class);

    }

    public static List<UserResponseDto> mappingToUserListResponse(List<UserModel> users){
        return users.stream().map(UserMapper::mappingToUserResponse).collect(Collectors.toList());
    }


}
