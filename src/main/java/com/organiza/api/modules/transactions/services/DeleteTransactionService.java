package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.transactions.domain.dtos.ShowOneUserTransactionDto;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repositories.TransactionRepository;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import com.organiza.api.modules.users.services.ShowUserService;
import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteTransactionService {

    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public void execute(String user_id, String id){

        userRepository.findById(UUID.fromString(user_id)).orElseThrow(
                () -> new HttpNotFoundException(
                        UserErrorConsts.USER_NOT_FOUND)
        );

     TransactionModel transaction = transactionRepository.findById(UUID.fromString(id)).orElseThrow(
                () ->
                new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND)

        );

    transactionRepository.delete(transaction);


    }


}
