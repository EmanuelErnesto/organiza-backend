package com.organiza.api.modules.users.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDto {

    @NotBlank(message = "name cannot be empty")
    @Size(min = 5, max = 200)
    private String name;

    @NotBlank
    @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.+-]+\\.[a-z]{2,}$", message = "invalid email format. Try again with a valid email.")
    private String email;

    @NotBlank
    @Size(min = 6, max = 50)
    private String password;
}
