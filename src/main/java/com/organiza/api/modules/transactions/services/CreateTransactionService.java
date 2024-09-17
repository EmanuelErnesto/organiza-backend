package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
import com.organiza.api.modules.transactions.processor.TransactionPaymentProcessor;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateTransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionPaymentProcessor paymentProcessor;

    @Transactional
    public TransactionModel execute(CreateTransactionDto transactionData) {

        Optional<UserModel> optionalUser = userRepository.findById(UUID.fromString(transactionData.getUser_id()));

        if (optionalUser.isEmpty()) throw new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND);

        UserModel user = optionalUser.get();

        TransactionModel transactionModel = transactionMapper.mappingToTransactionModel(transactionData);

        if (transactionData.getType().equals("DESPESA")) {
            TransactionModel transactionWithCreatedAt = transactionRepository.save(transactionModel);

            paymentProcessor.processExpense(transactionWithCreatedAt, user);

            return transactionWithCreatedAt;
        }

        return paymentProcessor.processIncome(transactionModel, user);

    }

}
