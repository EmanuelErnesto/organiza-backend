package com.organiza.api.modules.budgets.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.CreateBudgetDto;
import com.organiza.api.modules.budgets.domain.mappers.BudgetMapper;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.services.CreateBudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Budgets", description = "this endpoint can create, read, update and delete a budget")
@RestController
@RequestMapping("/api/v1/budgets")
public class CreateBudgetController {

    @Autowired
    private CreateBudgetService createBudgetService;

    @Autowired
    private BudgetMapper budgetMapper;

    @Operation(summary = "Create a new budget", description = "Resource for create a new budget.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Budget created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = BudgetResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<BudgetResponseDto> execute(@Valid @RequestBody CreateBudgetDto body) {

        BudgetModel budget = createBudgetService.execute(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(budgetMapper.mappingToBudgetResponse(budget));

    }

}
