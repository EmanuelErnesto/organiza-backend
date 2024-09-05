package com.organiza.api.modules.users.services;


import com.organiza.api.exception.HttpBadRequestException;
import com.organiza.api.modules.users.infra.database.entities.UserModel;
import com.organiza.api.modules.users.infra.database.repositories.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class UpdateUserPasswordService {

    private final UserRepository userRepository;
    private final ShowUserService showUserService;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserModel execute(UUID id, String oldPassword, String newPassword, String passwordConfirmation) {

        if(!newPassword.equals(passwordConfirmation)) throw new HttpBadRequestException(UserErrorConsts.PASSWORD_MUST_EQUAL);

        UserModel user = showUserService.execute(id);

        if(!passwordEncoder.matches(oldPassword, user.getPassword())) throw new HttpBadRequestException(UserErrorConsts.PASSWORD_DOES_NOT_MATCH);



        user.setPassword(passwordEncoder.encode(newPassword));

        return user;
    }

}
