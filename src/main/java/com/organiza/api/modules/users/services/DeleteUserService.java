package com.organiza.api.modules.users.services;

import com.organiza.api.exception.HttpNotFoundException;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DeleteUserService {

    private final UserRepository userRepository;

    public void execute(UUID id) {
        userRepository.findById(id).orElseThrow(() ->
                new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND));

        userRepository.deleteById(id);

    }
}
