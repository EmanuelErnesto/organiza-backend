package com.organiza.api.config;

import com.organiza.api.modules.transactions.domain.dtos.mappers.TransactionMapper;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Autowired
    private final UserRepository userRepository;

    public AppConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Bean
    public TransactionMapper transactionMapper() {
        return new TransactionMapper(userRepository);
    }
}
