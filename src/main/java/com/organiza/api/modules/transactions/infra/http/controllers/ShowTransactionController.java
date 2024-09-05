package com.organiza.api.modules.transactions.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.transactions.domain.dtos.ShowOneUserTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.services.ShowTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Transaction")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class ShowTransactionController {

    private final ShowTransactionService showTransactionService;

    @Operation(summary = "Show one transaction", description = "endpoint that retrieves one transaction of an existing user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Transaction retrieved successfully",
                            content = @Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDto.class)))),
                    @ApiResponse(responseCode = "403", description = "User doesn't have permission to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "401", description = "Access denied",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "Transaction not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "404", description = "User not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<TransactionResponseDto> execute(
            @PathVariable("id") String id,
            @Valid @RequestBody ShowOneUserTransactionDto body) {

        TransactionModel transaction = showTransactionService.execute(UUID.fromString(id), body);

        return ResponseEntity.ok(TransactionMapper.mappingToTransactionResponse(transaction));
    }
}
