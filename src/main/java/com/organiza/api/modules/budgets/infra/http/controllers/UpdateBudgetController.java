package com.organiza.api.modules.budgets.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.UpdateBudgetDto;
import com.organiza.api.modules.budgets.domain.mappers.BudgetMapper;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.services.UpdateBudgetService;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "Budgets")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/budgets")
public class UpdateBudgetController {

    private final UpdateBudgetService updateBudgetService;

    @Operation(summary = "Update one budget", description = "endpoint that update one budget of a existent user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "budget updated successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BudgetResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "User doesn't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "Budget not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            })

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<BudgetResponseDto> execute(
            @PathVariable("id") String id,
            @Valid @RequestBody UpdateBudgetDto body
            ) {

        BudgetModel budget = updateBudgetService.execute(body, id);

        return ResponseEntity.ok(BudgetMapper.mappingToBudgetResponse(budget));

    }
}
