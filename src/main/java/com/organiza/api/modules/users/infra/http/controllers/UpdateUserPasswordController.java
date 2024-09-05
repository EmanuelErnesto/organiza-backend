package com.organiza.api.modules.users.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.users.domain.dtos.UpdateUserPasswordDto;
import com.organiza.api.modules.users.services.UpdateUserPasswordService;
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
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@Tag(name = "Users")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users/{id}")
public class UpdateUserPasswordController {


    private final UpdateUserPasswordService updateUserPasswordService;

    @Operation(summary = "Update user password property", description = "Resource that update a user password property",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "204", description = "Password Updated Successfully."),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "400", description = "the password provided does not match with the current password or newPassword and passwordConfirmation does not match",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })

    @PatchMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> execute(@PathVariable @Valid UUID id, @Valid @RequestBody UpdateUserPasswordDto body){
        updateUserPasswordService.execute(id, body.getOldPassword(), body.getNewPassword(), body.getPasswordConfirmation());

        return ResponseEntity.noContent().build();
    }
}
