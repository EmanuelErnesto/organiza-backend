package com.organiza.api.modules.budgets.services;

import com.organiza.api.exception.HttpBadRequestException;
import com.organiza.api.modules.budgets.domain.UpdateBudgetDto;
import com.organiza.api.modules.budgets.domain.mappers.BudgetMapper;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
import com.organiza.api.shared.common.utils.consts.BudgetErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UpdateBudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Autowired
    private BudgetMapper budgetMapper;

    @Autowired
    private TransactionRepository transactionRepository;

    @Transactional
    public BudgetModel execute(UpdateBudgetDto budgetDto, String id) {

        BudgetModel budget = budgetMapper.mappingToBudgetModelFromUpdateBudgetDto(budgetDto);

        Optional<BudgetModel> budgetExists =  budgetRepository.
                findBudgetByTimeInterval(UUID.fromString(budgetDto.getUser_id()),
                        budgetDto.getCategory(),
                        budgetDto.getStart_date(),
                        budgetDto.getEnd_date()
                );

        if(budgetExists.isPresent() && budgetExists.get().getId() != UUID.fromString(id)) {
            throw new HttpBadRequestException(BudgetErrorConsts.
                    BUDGET_ALREADY_EXISTS_IN_INTERVAL);
        }

        Optional<List<TransactionModel>> transactionsOpt = transactionRepository.findByCategoryAndInterval(budgetDto.getCategory(),
                budgetDto.getStart_date(),
                budgetDto.getEnd_date());

        if(transactionsOpt.isEmpty()) return budgetRepository.save(budget);

        if(!Objects.equals(budgetDto.getCategory(), budgetExists.get().getCategory())) {
            budget.setAmount_spent(0);
            transactionsOpt.get().forEach(transaction ->
                    budget.setAmount_spent(budget.getAmount_spent() + transaction.getValue()));
        }

        return budgetRepository.save(budget);

    }
}
