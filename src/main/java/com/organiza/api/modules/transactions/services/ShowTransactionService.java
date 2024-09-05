package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.transactions.domain.dtos.ShowOneUserTransactionDto;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repositories.TransactionRepository;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShowTransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public TransactionModel execute(UUID transactionId, ShowOneUserTransactionDto userTransactionDto) {
        userRepository.findById(UUID.fromString(userTransactionDto.getUser_id())).orElseThrow(
                () -> new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND)
        );

        return transactionRepository.findById(transactionId).orElseThrow(
                () -> new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND)
        );
    }
}
