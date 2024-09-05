package com.organiza.api.modules.transactions.services;

import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.transactions.infra.database.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ListUserTransactionService {

    private final TransactionRepository transactionRepository;

    @Transactional(readOnly = true)
    public List<TransactionModel> execute(UUID id) {
        return transactionRepository.findAllByUserId(id);

    }

}