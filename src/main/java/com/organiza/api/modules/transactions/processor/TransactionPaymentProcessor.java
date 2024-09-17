package com.organiza.api.modules.transactions.processor;

import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class TransactionPaymentProcessor {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;

    public void processExpense(TransactionModel transactionModel, UserModel user) {
        user.setBalance(user.getBalance() - transactionModel.getValue());
        userRepository.save(user);

        Optional<BudgetModel> budgetOpt = budgetRepository.findByCategoryAndTransactionDate(
                transactionModel.getCategory(), transactionModel.getCreatedAt().toLocalDate()
        );

        if (budgetOpt.isPresent()) {
            BudgetModel budget = budgetOpt.get();
            budget.setAmount_spent(budget.getAmount_spent() + transactionModel.getValue());
            budgetRepository.save(budget);
        }
    }

    public TransactionModel processIncome(TransactionModel transactionModel, UserModel user) {
        user.setBalance(user.getBalance() + transactionModel.getValue());
        userRepository.save(user);
        return transactionRepository.save(transactionModel);
    }

    public void updateBudgetExpenseWithTransactionValue(CreateTransactionDto transactionDto, TransactionModel transactionModel, UserModel user) {
        Optional<BudgetModel> budgetOpt = budgetRepository.findByCategoryAndTransactionDate(
                transactionModel.getCategory(), transactionModel.getCreatedAt().toLocalDate()
        );

        if (budgetOpt.isPresent()) {
            BudgetModel budget = budgetOpt.get();

            if (categoryHasChanged(budget, transactionDto)) {
                handleCategoryChange(budget, transactionModel, transactionDto, user);
                return;
            }

            handleValueChange(budget, transactionDto, transactionModel);
            budgetRepository.save(budget);
        }
    }

    private boolean categoryHasChanged(BudgetModel budget, CreateTransactionDto transactionDto) {
        return !Objects.equals(budget.getCategory(), transactionDto.getCategory());
    }

    private void handleCategoryChange(BudgetModel budget, TransactionModel transactionModel, CreateTransactionDto transactionDto, UserModel user) {
        budget.setAmount_spent(budget.getAmount_spent() - transactionModel.getValue());
        TransactionModel updatedTransaction = transactionRepository.save(transactionModel);
        processExpense(updatedTransaction, user);
    }

    private void handleValueChange(BudgetModel budget, CreateTransactionDto transactionDto, TransactionModel transactionModel) {
        double difference;
        if (transactionDto.getValue() > transactionModel.getValue()) {
            difference = transactionDto.getValue() - transactionModel.getValue();
            budget.setAmount_spent(budget.getAmount_spent() + difference);
            return;
        }
            difference = transactionModel.getValue() - transactionDto.getValue();
            budget.setAmount_spent(budget.getAmount_spent() - difference);

    }


    }


