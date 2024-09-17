package com.organiza.api.modules.budgets.services;

import com.organiza.api.exception.HttpBadRequestException;
import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.budgets.domain.CreateBudgetDto;
import com.organiza.api.modules.budgets.domain.mappers.BudgetMapper;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.shared.common.utils.consts.BudgetErrorConsts;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;

import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Slf4j
public class CreateBudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public BudgetModel execute(CreateBudgetDto budgetDto) {

        BudgetModel budget = budgetMapper.mappingToBudgetModel(budgetDto);

        Optional<BudgetModel> budgetExists =  budgetRepository.
                findBudgetByTimeInterval(UUID.fromString(budgetDto.getUser_id()),
                budgetDto.getCategory(),
                budgetDto.getStart_date(),
                budgetDto.getEnd_date()
                );

        if(budgetExists.isPresent()) {
            throw new HttpBadRequestException(BudgetErrorConsts.
                    BUDGET_ALREADY_EXISTS_IN_INTERVAL);
        }

       Optional<List<TransactionModel>> transactionsOpt = transactionRepository.findByCategoryAndInterval(budgetDto.getCategory(),
                budgetDto.getStart_date(),
                budgetDto.getEnd_date());

        if(transactionsOpt.isEmpty()) return budgetRepository.save(budget);


        List<TransactionModel> transactions = transactionsOpt.get();

        transactions.forEach(transaction ->
                budget.setAmount_spent(budget.getAmount_spent() + transaction.getValue()));

        return budgetRepository.save(budget);

    }


}
