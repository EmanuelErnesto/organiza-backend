package com.organiza.api.modules.users.domain.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserAuthenticationDto {


    @NotBlank
    @Email(regexp = "^[a-z0-9.+-]+@[a-z0-9.+-]+\\.[a-z]{2,}$", message = "invalid email format. Try again with a valid email.")
    private String email;

    @NotBlank(message = "password cannot be empty")
    @Size(min = 6, max = 50)
    private String password;


}
