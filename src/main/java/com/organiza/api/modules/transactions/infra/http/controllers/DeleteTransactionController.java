package com.organiza.api.modules.transactions.infra.http.controllers;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.transactions.domain.dtos.ShowOneUserTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.services.DeleteTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Transaction")
@RestController
@RequestMapping("/api/v1/transactions")
public class DeleteTransactionController {

    @Autowired
    private DeleteTransactionService deleteTransactionService;

    @Operation(summary = "delete one transaction", description = "endpoint that delete one existent transaction.",
            responses = {
                    @ApiResponse(responseCode = "204", description = "Transaction deleted successfully",
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
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> execute(@PathVariable("id") String id, @Valid @RequestBody ShowOneUserTransactionDto body) {

        deleteTransactionService.execute(body.getUser_id(), id);

        return ResponseEntity.noContent().build();

    }


}
