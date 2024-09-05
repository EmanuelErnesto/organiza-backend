package com.organiza.api.modules.users.domain.dtos;

import lombok.*;

import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class UserResponseDto {
    private UUID id;
    private String name;
    private String email;
    private double balance;
    private String role;

}
