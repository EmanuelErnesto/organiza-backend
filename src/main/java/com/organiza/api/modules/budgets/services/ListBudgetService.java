package com.organiza.api.modules.budgets.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.mappers.BudgetMapper;
import com.organiza.api.modules.budgets.infra.database.repository.BudgetRepository;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ListBudgetService {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final BudgetMapper budgetMapper;

    @Transactional(readOnly = true)
    public List<BudgetResponseDto> execute(String user_id) {
        userRepository.findById(UUID.fromString(user_id)).orElseThrow(() ->
                new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND));

        return budgetRepository.findAll().stream().map(BudgetMapper::mappingToBudgetResponse).toList();

    }

}
