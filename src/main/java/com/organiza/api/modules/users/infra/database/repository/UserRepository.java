package com.organiza.api.modules.users.infra.database.repositories;

import com.organiza.api.modules.users.infra.database.entities.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;


public interface UserRepository extends JpaRepository<UserModel, UUID> {

    public Optional<UserModel> findByEmail(String email);

    @Query("SELECT u.role FROM UserModel u WHERE u.email = :email")
    Optional<UserModel.Role> findRoleByEmail(String email);

}
