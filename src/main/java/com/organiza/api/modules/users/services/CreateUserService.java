package com.organiza.api.modules.users.services;

import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import com.organiza.api.exception.HttpBadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CreateUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserModel execute(UserModel user){
        Optional<UserModel> userEmailExists = userRepository.findByEmail(user.getEmail());

        if(userEmailExists.isPresent()) throw new HttpBadRequestException(UserErrorConsts.EMAIL_ALREADY_USED);

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }
}
