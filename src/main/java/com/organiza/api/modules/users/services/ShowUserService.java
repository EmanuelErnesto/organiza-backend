package com.organiza.api.modules.users.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ShowUserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserModel execute(UUID id) {
        return userRepository.findById(id).orElseThrow(() ->
                new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND));
    }

}
