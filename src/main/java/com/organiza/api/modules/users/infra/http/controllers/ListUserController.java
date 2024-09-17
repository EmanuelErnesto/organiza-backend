package com.organiza.api.modules.users.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import com.organiza.api.modules.users.domain.dtos.mappers.UserMapper;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.services.ListUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
public class ListUserController {

    private final ListUserService listUserService;

    @Operation(summary = "List registered users", description = "Resource that returns a list of the all registered users",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users returned successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema =
                                    @Schema(implementation = UserResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            })
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> execute() {
        List<UserModel> users = listUserService.execute();

        return ResponseEntity.ok(UserMapper.mappingToUserListResponse(users));

    }

}
