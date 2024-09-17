package com.organiza.api.modules.transactions.infra.database.repository;

import com.organiza.api.modules.transactions.infra.database.entity.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {

    @Query("SELECT t FROM TransactionModel t WHERE t.user.id = :userId")
    List<TransactionModel> findAllByUserId(UUID userId);

    @Query(value = """
            SELECT * FROM tb_transactions t\s
            WHERE LOWER(t.category) = LOWER(:category) AND
            t.type_transaction = 'DESPESA' AND
           \s
            (t.created_at BETWEEN :start_date AND :end_date)""", nativeQuery = true)
    Optional<List<TransactionModel>> findByCategoryAndInterval(String category, LocalDate start_date, LocalDate end_date);
}
