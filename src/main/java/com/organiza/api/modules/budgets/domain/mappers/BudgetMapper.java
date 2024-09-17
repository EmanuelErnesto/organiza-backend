package com.organiza.api.modules.budgets.domain.mappers;

import com.organiza.api.modules.budgets.domain.BudgetResponseDto;
import com.organiza.api.modules.budgets.domain.CreateBudgetDto;
import com.organiza.api.modules.budgets.domain.UpdateBudgetDto;
import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class BudgetMapper {

    private final UserRepository userRepository;


    public BudgetModel mappingToBudgetModel(CreateBudgetDto createBudgetDto) {
        BudgetModel budget = new ModelMapper().map(createBudgetDto, BudgetModel.class);

        userRepository.findById(UUID.fromString(createBudgetDto.getUser_id())).ifPresent(budget::setUser_id);

        return budget;
    }

    public static BudgetResponseDto mappingToBudgetResponse(BudgetModel budgetEntity) {
        return new ModelMapper().map(budgetEntity, BudgetResponseDto.class);
    }

    public BudgetModel mappingToBudgetModelFromUpdateBudgetDto(UpdateBudgetDto updateBudgetDto) {
        BudgetModel budget = new ModelMapper().map(updateBudgetDto, BudgetModel.class);

        userRepository.findById(UUID.fromString(updateBudgetDto.getUser_id())).ifPresent(budget::setUser_id);

        return budget;
    }


}
