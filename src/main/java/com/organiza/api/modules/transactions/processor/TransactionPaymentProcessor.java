package com.organiza.api.modules.transactions.processor;

import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class TransactionPaymentProcessor {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

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

    public void processInvestmentOfABudgetExistentDelete(TransactionModel transactionModel, UserModel user){
        user.setBalance(user.getBalance() + transactionModel.getValue());
        userRepository.save(user);

        Optional<BudgetModel> budgetOpt = budgetRepository.findByCategoryAndTransactionDate(
                transactionModel.getCategory(), transactionModel.getCreatedAt().toLocalDate()
        );

        if (budgetOpt.isPresent()) {
            BudgetModel budget = budgetOpt.get();
            budget.setAmount_spent(budget.getAmount_spent() - transactionModel.getValue());
            budgetRepository.save(budget);
        }
    }

    public TransactionModel processIncome(TransactionModel transactionModel, UserModel user) {
        user.setBalance(user.getBalance() + transactionModel.getValue());
        userRepository.save(user);
        return transactionRepository.save(transactionModel);
    }

    public void updateBudgetExpenseWithTransactionValue(CreateTransactionDto transactionDto, TransactionModel transactionModel, UserModel user) {
        Optional<BudgetModel> oldBudget = budgetRepository.findByCategoryAndTransactionDate(
                transactionModel.getCategory().toLowerCase(), transactionModel.getCreatedAt().toLocalDate()
        );

        if(oldBudget.isPresent()) {
            BudgetModel oldBudgetPresent = oldBudget.get();

            if(categoryHasChanged(oldBudgetPresent, transactionDto)) {

                handleValueChange(oldBudgetPresent, transactionDto, transactionModel);
                oldBudgetPresent.setAmount_spent(oldBudgetPresent.getAmount_spent() - transactionModel.getValue());
                budgetRepository.save(oldBudgetPresent);
            }

            transactionModel.setCategory(transactionDto.getCategory().toLowerCase());
            transactionModel.setDescription(transactionDto.getDescription());
            transactionModel.setValue(transactionDto.getValue());
            transactionModel.setStatus(TransactionModel.Status.valueOf(transactionDto.getStatus()));
            transactionModel.setDate_payment(transactionDto.getDate_payment());
            transactionRepository.save(transactionModel);

         Optional<BudgetModel> newBudget = budgetRepository.findByCategoryAndTransactionDate(
                 transactionDto.getCategory().toLowerCase(), transactionModel.getCreatedAt().toLocalDate()
         );

         if(newBudget.isPresent()) {
             BudgetModel newBudgetPresent = newBudget.get();
             newBudgetPresent.setAmount_spent(newBudgetPresent.getAmount_spent() + transactionModel.getValue());
             budgetRepository.save(newBudgetPresent);
         }


        }

    }


    private boolean categoryHasChanged(BudgetModel budget, CreateTransactionDto transactionDto) {
        return !Objects.equals(budget.getCategory().toLowerCase(), transactionDto.getCategory().toLowerCase());
    }

    private void handleCategoryChange(BudgetModel budget, TransactionModel transactionModel, CreateTransactionDto transactionDto, UserModel user) {
        budget.setAmount_spent(budget.getAmount_spent() - transactionModel.getValue());
        transactionModel.setCategory(transactionDto.getCategory().toLowerCase());
        TransactionModel updatedTransaction = transactionRepository.save(transactionMapper.mappingToTransactionModel(transactionDto));
        processExpense(updatedTransaction, user);
    }

    private void handleValueChange(BudgetModel budget, CreateTransactionDto transactionDto, TransactionModel transactionModel) {
        double oldValue = transactionModel.getValue();
        double newValue = transactionDto.getValue();
        double difference = newValue - oldValue;
        budget.setAmount_spent(budget.getAmount_spent() + difference);
    }


    }


