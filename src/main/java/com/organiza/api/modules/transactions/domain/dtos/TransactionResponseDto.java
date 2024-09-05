package com.organiza.api.modules.transactions.domain.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {

    private UUID id;
    private String type;
    private String category;
    private double value;
    private String description;
    private String status;
    private String date_payment;

}
