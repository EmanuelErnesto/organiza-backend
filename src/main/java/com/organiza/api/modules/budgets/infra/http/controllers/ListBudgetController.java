package com.organiza.api.modules.budgets.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.GetBudgetDto;
import com.organiza.api.modules.budgets.services.ListBudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Budgets")
@RestController
@RequestMapping("/api/v1/budgets")
public class ListBudgetController {

    @Autowired
    private ListBudgetService listBudgetService;

    @Operation(summary = "List budgets", description = "endpoint that list all budgets of a user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Budgets retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BudgetResponseDto.class)
                                    ))),
                    @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            })
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<BudgetResponseDto>> execute(@Valid @RequestBody GetBudgetDto budgetDto) {

     List<BudgetResponseDto> budgets = listBudgetService.execute(budgetDto.getId());

     return ResponseEntity.ok(budgets);

    }

}
