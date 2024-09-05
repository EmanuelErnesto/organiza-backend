package com.organiza.api.modules.users.domain.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateUserPasswordDto {

    @NotBlank
    @Size(min = 6, max = 50)
    private String oldPassword;

    @NotBlank
    @Size(min = 6, max = 50)
    private String newPassword;

    @NotBlank
    @Size(min = 6, max = 50)
    private String passwordConfirmation;
}
