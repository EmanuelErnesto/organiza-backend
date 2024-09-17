package com.organiza.api.modules.budgets.infra.database.repository;

import com.organiza.api.modules.budgets.infra.database.entity.BudgetModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface BudgetRepository extends JpaRepository<BudgetModel, UUID> {

    @Query(value = """
         SELECT *
         FROM tb_budgets B
         WHERE
             B.user_id = :user_id
             AND LOWER(B.category) = LOWER(:category)
             AND (
                 (:start_date BETWEEN B.start_date AND B.end_date OR
                 :end_date BETWEEN B.start_date AND B.end_date) OR
                 (B.start_date BETWEEN :start_date AND :end_date OR
                 B.end_date BETWEEN :start_date AND :end_date)
             );
     """, nativeQuery = true)
    Optional<BudgetModel> findBudgetByTimeInterval(UUID user_id, String category, LocalDate start_date, LocalDate end_date);

    @Query(value = """
          SELECT *
          FROM tb_budgets B
          WHERE LOWER(B.category) = LOWER(:category)
          AND (
          :transaction_date BETWEEN B.start_date AND B.end_date)""", nativeQuery = true)
    Optional<BudgetModel> findByCategoryAndTransactionDate(String category, LocalDate transaction_date);
}
