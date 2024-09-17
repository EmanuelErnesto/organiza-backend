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
import com.organiza.api.modules.users.services.ShowUserService;
import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;


@RequiredArgsConstructor
@Service
public class UpdateTransactionService {

    private final ShowUserService showUserService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final TransactionPaymentProcessor paymentProcessor;
    private final BudgetRepository budgetRepository;

    @Transactional
    public TransactionModel execute(String id, CreateTransactionDto transactionDto){

        TransactionModel transactionExists = transactionRepository.findById(
                        UUID.fromString(id))
                .orElseThrow(() -> new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND));

        UserModel user = showUserService.execute(UUID.fromString(transactionDto.getUser_id()));

        TransactionModel transaction = transactionMapper.mappingToTransactionModel(transactionDto);


        if(transactionDto.getType().equals("DESPESA")) {
            paymentProcessor.updateBudgetExpenseWithTransactionValue(transactionDto, transaction, user);

        }

        return transactionRepository.save(transaction);

    }

}
