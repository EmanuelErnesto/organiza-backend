package com.organiza.api.modules.transactions.infra.database.repositories;

import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface TransactionRepository extends JpaRepository<TransactionModel, UUID> {

    @Query("SELECT t FROM TransactionModel t WHERE t.user.id = :userId")
    List<TransactionModel> findAllByUserId(UUID userId);

}
