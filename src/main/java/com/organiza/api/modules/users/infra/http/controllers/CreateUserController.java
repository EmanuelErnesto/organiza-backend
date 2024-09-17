package com.organiza.api.modules.users.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;

import com.organiza.api.modules.users.domain.dtos.CreateUserDto;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import com.organiza.api.modules.users.domain.dtos.mappers.UserMapper;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.services.CreateUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Users", description = "This endpoint can create, read, update and delete a user.")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class CreateUserController {

    private final CreateUserService createUserService;

    @Operation(summary = "Create a new User", description = "Resource for create a new user.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "User created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "email or password invalid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "400", description = "Email already registered in the system.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDto> execute (@Valid @RequestBody CreateUserDto userDto) {
        UserModel user = createUserService.execute(UserMapper.mappingToUser(userDto));

        return ResponseEntity.status(HttpStatus.CREATED).body(UserMapper.mappingToUserResponse(user));

    }



}
