package com.organiza.api.modules.budgets.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.GetBudgetDto;
import com.organiza.api.modules.budgets.services.DeleteBudgetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Budgets")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/budgets")
public class DeleteBudgetController {

    private final DeleteBudgetService deleteBudgetService;

    @Operation(summary = "delete one transaction", description = "endpoint that delete one existent transaction.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = BudgetResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "User doesn't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> execute(@Valid @PathVariable("id") GetBudgetDto pathBudgetId, @Valid @RequestBody GetBudgetDto bodyUserId) {

        deleteBudgetService.execute(bodyUserId.getId(), pathBudgetId.getId());

        return ResponseEntity.noContent().build();

    }


}
