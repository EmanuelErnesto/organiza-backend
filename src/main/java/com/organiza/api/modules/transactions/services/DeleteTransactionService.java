package com.organiza.api.modules.transactions.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
import com.organiza.api.modules.transactions.processor.TransactionPaymentProcessor;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
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
    private final BudgetRepository budgetRepository;
    private final TransactionPaymentProcessor paymentProcessor;

    public void execute(String user_id, String id){

      UserModel user = userRepository.findById(UUID.fromString(user_id)).orElseThrow(
                () -> new HttpNotFoundException(
                        UserErrorConsts.USER_NOT_FOUND)
        );

     TransactionModel transaction = transactionRepository.findById(UUID.fromString(id)).orElseThrow(
                () ->
                new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND)

        );

        if (transaction.getType().toString().equals("DESPESA")) {
            TransactionModel transactionWithCreatedAt = transactionRepository.save(transaction);

            paymentProcessor.processInvestmentOfABudgetExistentDelete(transactionWithCreatedAt, user);

            return;
        }

    transactionRepository.delete(transaction);


    }


}
