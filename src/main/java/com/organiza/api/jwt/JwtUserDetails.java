package com.organiza.api.jwt;

import com.organiza.api.modules.users.infra.database.entity.UserModel;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import java.util.UUID;


public class JwtUserDetails extends User {

    private final UserModel user;


    public JwtUserDetails(UserModel user) {
        super(user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }

    public UUID getId(){
        return this.user.getId();
    }

    public String getRole(){
        return this.user.getRole().name();
    }


}
