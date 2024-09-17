package com.organiza.api.jwt;

import com.organiza.api.modules.users.infra.database.entity.UserModel;
import com.organiza.api.modules.users.infra.database.repository.UserRepository;
import com.organiza.api.shared.common.utils.consts.UserErrorConsts;
import com.organiza.api.exception.HttpNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserModel user = userRepository.findByEmail(email).orElseThrow(
                () -> new HttpNotFoundException(String.format("user with email '%s' not found", email))
        );

        return new JwtUserDetails(user);

    }

    public JwtToken getTokenAuthenticated(String email) {
        UserModel.Role role = userRepository.findRoleByEmail(email).orElseThrow(() ->
                new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND)
        );
        UUID userId = userRepository.findUserIdByEmail(email).orElseThrow(
                () -> new HttpNotFoundException(UserErrorConsts.USER_NOT_FOUND)
        );

        return JwtUtils.createToken(email, userId ,role.name().substring("ROLE_".length()));
    }
}
