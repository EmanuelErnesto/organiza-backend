package com.organiza.api.modules.users.infra.http.controllers;


import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import com.organiza.api.modules.users.domain.dtos.mappers.UserMapper;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.services.ShowUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/{id}")
public class ShowUserController {

    private final ShowUserService showUserService;

    @Operation(summary = "Find a user by id", description = "Resource that return a user through the id",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User returned successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') OR ( hasRole('CLIENT') AND #id == authentication.principal.id)")
    public ResponseEntity<UserResponseDto> execute(@Valid @PathVariable UUID id) {
        UserModel user = showUserService.execute(id);

        return ResponseEntity.ok(UserMapper.mappingToUserResponse(user));
    }

}
