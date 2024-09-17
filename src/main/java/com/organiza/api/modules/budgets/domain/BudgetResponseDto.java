package com.organiza.api.modules.budgets.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BudgetResponseDto {

    private UUID id;
    private String category;
    private double estimated_value;
    private double amount_spent;
    private String start_date;
    private String end_date;

}
