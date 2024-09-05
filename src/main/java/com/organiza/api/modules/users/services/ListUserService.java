package com.organiza.api.modules.users.services;

import com.organiza.api.modules.users.infra.database.entities.UserModel;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ListUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserModel> execute(){
        return userRepository.findAll();
    }


}
