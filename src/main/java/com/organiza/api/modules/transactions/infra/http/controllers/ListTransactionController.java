package com.organiza.api.modules.transactions.infra.http.controllers;


import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.transactions.domain.dtos.FindUserTransactionsDto;
import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.services.ListUserTransactionService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Tag(name = "Transaction")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/transactions")
public class ListTransactionController {

    private final ListUserTransactionService listUserTransactionService;

    @Operation(summary = "List transactions", description = "endpoint that list all transactions of a user",
    responses = {
            @ApiResponse(responseCode = "200", description = "Transactions retrieved successfully",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = TransactionResponseDto.class)
            ))),
            @ApiResponse(responseCode = "403", description = "User don't have permission to access this resource",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
            @ApiResponse(responseCode = "401", description = "Access denied",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
    })
    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'CLIENT')")
    public ResponseEntity<List<TransactionResponseDto>> execute(@Valid @RequestBody FindUserTransactionsDto body){

        List<TransactionModel> transactions = listUserTransactionService.execute(UUID.fromString(body.getUser_id()));

        return ResponseEntity.ok(TransactionMapper.mappingToTransactionListResponse(transactions));

    }

}
