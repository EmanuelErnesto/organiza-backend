package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repositories.TransactionRepository;
import com.organiza.api.modules.users.services.ShowUserService;
import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UpdateTransactionService {

    private final ShowUserService showUserService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionModel execute(String id, CreateTransactionDto transactionDto){

        transactionRepository.findById(
                        UUID.fromString(id))
                .orElseThrow(() -> new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND));

        showUserService.execute(UUID.fromString(transactionDto.getUser_id()));

        TransactionModel transaction = transactionMapper.mappingToTransactionModel(transactionDto);

        return transactionRepository.save(transaction);

    }



}
