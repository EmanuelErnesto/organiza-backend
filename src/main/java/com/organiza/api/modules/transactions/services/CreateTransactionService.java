package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repositories.TransactionRepository;
import com.organiza.api.modules.users.infra.database.entities.UserModel;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CreateTransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;

    @Transactional
    public TransactionModel execute(CreateTransactionDto transactionData) {

        TransactionModel transactionModel = transactionMapper.mappingToTransactionModel(transactionData);

        UserModel userExists = transactionModel.getUser();

        if (userExists == null) throw new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND);


        if (transactionData.getType().toString().equals("DESPESA")) {
            userExists.setBalance(userExists.getBalance() - transactionData.getValue());
            userRepository.save(userExists);
            return transactionRepository.save(transactionModel);
        }
        userExists.setBalance(userExists.getBalance() + transactionData.getValue());

        userRepository.save(userExists);

        return transactionRepository.save(transactionModel);
    }
}
