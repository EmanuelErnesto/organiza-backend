    package com.organiza.api.modules.transactions.services;

    import com.organiza.api.exception.HttpNotFoundException;
    import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
    import com.organiza.api.modules.transactions.domain.dtos.UpdateTransactionDto;
    import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
    import com.organiza.api.modules.transactions.infra.database.repository.TransactionRepository;
    import com.organiza.api.modules.transactions.processor.TransactionPaymentProcessor;
    import com.organiza.api.modules.users.infra.database.entity.UserModel;
    import com.organiza.api.modules.users.services.ShowUserService;
    import com.organiza.api.shared.common.utils.consts.TransactionsErrorConsts;
    import lombok.RequiredArgsConstructor;
    import lombok.extern.slf4j.Slf4j;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.UUID;

    @Slf4j
    @RequiredArgsConstructor
    @Service
    public class UpdateTransactionService {

        private final ShowUserService showUserService;
        private final TransactionRepository transactionRepository;
        private final TransactionPaymentProcessor paymentProcessor;

        @Transactional
        public TransactionModel execute(String id, CreateTransactionDto transactionDto) {
            TransactionModel transactionExists = transactionRepository.findById(UUID.fromString(id))
                    .orElseThrow(() -> new HttpNotFoundException(TransactionsErrorConsts.TRANSACTION_NOT_FOUND));

            UserModel user = showUserService.execute(UUID.fromString(transactionDto.getUser_id()));

            if (transactionDto.getType().equals("DESPESA")) {
                paymentProcessor.updateBudgetExpenseWithTransactionValue(transactionDto, transactionExists, user);
            }


            return transactionRepository.save(transactionExists);
        }



    }
