package com.organiza.api.modules.transactions.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;

import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.services.CreateTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Transaction", description = "this endpoint can create, read, update and delete transactions")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class CreateTransactionController {

    private final CreateTransactionService createTransactionService;

    @Operation(summary = "Create a new transaction", description = "Resource for create a new transaction.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Transaction created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = TransactionResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthenticated",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "403", description = "Access denied.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })


    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<TransactionResponseDto> execute(@Valid @RequestBody CreateTransactionDto body){

        TransactionModel transaction = createTransactionService.execute(body);

        return ResponseEntity.status(HttpStatus.CREATED).body(TransactionMapper.mappingToTransactionResponse(transaction));
    }
}
