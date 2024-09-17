package com.organiza.api.modules.budgets.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.BudgetErrorConsts;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShowBudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public BudgetModel execute(String user_id, String id) {
        userRepository.findById(UUID.fromString(user_id)).orElseThrow(() ->
                new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND));

        return budgetRepository.findById(UUID.fromString(id)).orElseThrow(
                () -> new HttpNotFoundException(BudgetErrorConsts.BUDGET_NOT_FOUND)
        );
    }
}
