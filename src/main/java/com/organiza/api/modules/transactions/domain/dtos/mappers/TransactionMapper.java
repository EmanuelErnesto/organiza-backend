package com.organiza.api.modules.transactions.domain.dtos.mappers;

import com.organiza.api.modules.transactions.domain.dtos.CreateTransactionDto;
import com.organiza.api.modules.transactions.domain.dtos.TransactionResponseDto;
import com.organiza.api.modules.transactions.domain.dtos.UpdateTransactionDto;
import com.organiza.api.modules.transactions.infra.database.entities.TransactionModel;
import com.organiza.api.modules.users.infra.database.entities.UserModel;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TransactionMapper {


    private final UserRepository userRepository;


    public TransactionModel mappingToTransactionModel(CreateTransactionDto createTransactionDto) {
        TransactionModel transactionModel = new ModelMapper().map(createTransactionDto, TransactionModel.class);

        userRepository.findById(UUID.fromString(createTransactionDto.getUser_id())).ifPresent(transactionModel::setUser);

        return transactionModel;
    }

    public static TransactionResponseDto mappingToTransactionResponse(TransactionModel transaction) {
        String type = transaction.getType().toString().toLowerCase();
        String status = transaction.getStatus().toString().toLowerCase();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String date = transaction.getDate_payment().format(formatter);

        PropertyMap<TransactionModel, TransactionResponseDto> props = new PropertyMap<TransactionModel, TransactionResponseDto>() {
            @Override
            protected void configure() {
                map().setType(type);
                map().setStatus(status);
                map().setDate_payment(date);
            }
        };
        ModelMapper mapper = new ModelMapper();
        mapper.addMappings(props);

        return mapper.map(transaction, TransactionResponseDto.class);
    }

    public static List<TransactionResponseDto> mappingToTransactionListResponse(List<TransactionModel> transactions){
        return transactions.stream().map(TransactionMapper::mappingToTransactionResponse).collect(Collectors.toList());
    }

    public TransactionModel mappingUpdateTransactionDtoToTransactionModel(UpdateTransactionDto updateTransactionDto) {

        TransactionModel transactionModel = new ModelMapper().map(updateTransactionDto, TransactionModel.class);


        userRepository.findById(UUID.fromString(updateTransactionDto.getUser_id())).ifPresent(transactionModel::setUser);

        return transactionModel;

    }

}
