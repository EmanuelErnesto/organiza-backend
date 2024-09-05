package com.organiza.api.modules.transactions.domain.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UpdateTransactionDto {

    @NotBlank
    @Pattern(regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$", message = "Invalid UUID format")
    private String user_id;

    @NotBlank
    @Pattern(regexp = "RECEITA|DESPESA", message = "Invalid type. Must be RECEITA or DESPESA.")
    @Length(max = 10)
    private String type;

    @NotBlank
    @Pattern(regexp = "PENDING|FINISHED", message = "Invalid status. Must be PENDING OR FINISHED.")
    @Length(max = 10)
    private String status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate date_payment;

    @NotBlank
    @Size(min = 5, max = 150)
    private String category;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @Positive(message = "Value must be greather than zero.")
    private double value;

    @NotBlank
    @Size(min = 10, max = 200)
    private String description;


}
