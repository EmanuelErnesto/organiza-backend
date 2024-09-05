package com.organiza.api;


import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.modules.users.domain.dtos.CreateUserDto;
import com.organiza.api.modules.users.domain.dtos.UpdateUserPasswordDto;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/clear-users-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsersIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsers_WithEmailAndPasswordValid_ReturnsCreatedUserWithStatus201(){
        String name = "josue";
        String email = "josue@email.com";
        String password = "senha123";


        UserResponseDto responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getEmail()).isEqualTo(email);
        Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENT");
    }

    @Test
    public void createUsers_WithEmailEmpty_ReturnsMethodNotAllowedError422(){
        String name = "Josue";
        String email = "";
        String password = "senha123";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }

    @Test
    public void createUsers_WithEmailInvalid_ReturnsMethodNotAllowedError422(){
        String name = "janilson";
        String email = "janilson@";
        String password = "senha123";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }

    @Test
    public void createUsers_WithPasswordEmpty_ReturnsMethodNotAllowedError422(){
        String  name = "janilson";
        String email = "janilson@email.com";
        String password = "";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }


    @Test
    public void createUsers_WithPasswordInvalid_ReturnsMethodNotAllowedError422(){
        String name = "janilson";
        String email = "janilson@email.com";
        String password = "xd";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }

    @Test
    public void createUsers_WithEmailThatAlreadyExistsInDatabase_ReturnsErrorStatus400(){
        String name = "Ana Maria";
        String email = "ana@email.com";
        String password = "senha123456";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new CreateUserDto(name, email, password))
                .exchange()
                .expectStatus().isEqualTo(400)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(400);

    }

    @Test
    public void findUser_WithExistentId_ReturnsStatus200(){

        String email = "joao@email.com";
        String password = "123456";

        UserResponseDto responseBody = testClient
                .get()
                .uri("/api/v1/users/7e5f9728-09d1-4b83-a062-07f594f7b6a1")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBody(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();

    }

    @Test
    public void findUser_WithIdThatNotExistsInDatabase_ReturnsErrorStatus404(){

        String email = "joao@email.com";
        String password = "123456";

        ApplicationError responseBody = testClient
                .get()
                .uri("/api/v1/users/2d5f9728-09d1-4b83-a063-07f594f8b6b7")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(404)
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(404);
    }

    @Test
    public void findUsers_ReturnsStatus200(){

        String email = "joao@email.com";
        String password = "123456";

        List<UserResponseDto> responseBody = testClient
                .get()
                .uri("/api/v1/users")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, email, password))
                .exchange()
                .expectStatus().isEqualTo(200)
                .expectBodyList(UserResponseDto.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.size()).isEqualTo(3);

    }

    @Test
    public void updateUser_WithValidNewPassword_ReturnsStatus204(){

        String userEmail = "joao@email.com";
        String userPassword = "123456";

        String oldPassword = "123456";
        String newPassword = "123456";
        String passwordConfirmation = newPassword;

        testClient
                .patch()
                .uri("/api/v1/users/7e5f9728-09d1-4b83-a062-07f594f7b6a1")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto(oldPassword, newPassword, passwordConfirmation))
                .exchange()
                .expectStatus().isNoContent();

    }

    @Test
    public void updateUser_WithIdOfOtherUser_ReturnsErrorStatus403(){

        String userEmail = "joao@email.com";
        String userPassword = "123456";

        String oldPassword = "123456";
        String newPassword = "123456";
        String passwordConfirmation = newPassword;

       ApplicationError responseBody = testClient
                .patch()
                .uri("/api/v1/users/2d5f9728-09d1-4b83-a063-07f594f8b6b7")
               .headers(JwtAuthentication.getHeaderJwtToken(testClient, userEmail, userPassword))
               .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto(oldPassword, newPassword, passwordConfirmation))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(403);

    }

    @Test
    public void updateUser_WithInvalidOldPassword_ReturnsErrorStatus400(){
        String userEmail = "joao@email.com";
        String userPassword = "123456";


        String oldPassword = "Senha123";
        String newPassword = "123456";
        String passwordConfirmation = newPassword;

        ApplicationError responseBody = testClient
                .patch()
                .uri("/api/v1/users/7e5f9728-09d1-4b83-a062-07f594f7b6a1")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto(oldPassword, newPassword, passwordConfirmation))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(400);

    }

    @Test
    public void updateUser_WhenNewPasswordDoesNotMatchesWithPasswordConfirmation_ReturnsErrorStatus400(){
        String userEmail = "joao@email.com";
        String userPassword = "123456";


        String oldPassword = "123456";
        String newPassword = "123456";
        String passwordConfirmation = "novasenhaerro";

        ApplicationError responseBody = testClient
                .patch()
                .uri("/api/v1/users/7e5f9728-09d1-4b83-a062-07f594f7b6a1")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, userEmail, userPassword))
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UpdateUserPasswordDto(oldPassword, newPassword, passwordConfirmation))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(400);

    }

    @Test
    public void deleteUser_WithExistentId_Returns204(){

        String email = "joao@email.com";
        String password = "123456";


        var responseBody = testClient
                .delete()
                .uri("/api/v1/users/7e5f9728-09d1-4b83-a062-07f594f7b6a1")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, email, password))
                .exchange()
                .expectStatus().isNoContent()
                 .expectBody().isEmpty();

    }

    @Test
    public void deleteUser_WithIdOfOtherUser_ReturnsHttpStatus403(){

        String email = "joao@email.com";
        String password = "123456";


        ApplicationError responseBody = testClient
                .delete()
                .uri("/api/v1/users/2d5f9728-09d1-4b83-a063-07f594f8b6b7")
                .headers(JwtAuthentication.getHeaderJwtToken(testClient, email, password))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ApplicationError.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(403);

    }

}
