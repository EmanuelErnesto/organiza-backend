package com.organiza.api;

import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.jwt.JwtToken;
import com.organiza.api.modules.users.domain.dtos.UserAuthenticationDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/users/insert-users.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/users/clear-users-table.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AuthenticationIT {

    @Autowired
    WebTestClient testClient;

    @Test
    public void authenticate_WithValidCredentials_ReturnTokenWithStatus200(){

        String email = "joao@email.com";
        String password =  "123456";

        JwtToken responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();

    }

    @Test
    public void authenticate_WithInvalidEmail_ReturnTokenWithStatus400(){

        String email = "randomemail@email.com";
        String password =  "123456";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApplicationError.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(400);

    }


    @Test
    public void authenticate_WithInvalidPassword_ReturnTokenWithStatus400(){

        String email = "joao@email.com";
        String password =  "randompassword";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isBadRequest()
                .expectBody(ApplicationError.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(400);

    }

    @Test
    public void authenticate_WithEmptyEmail_ReturnTokenWithStatus422(){

        String email = "";
        String password =  "123456";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }

    @Test
    public void authenticate_WithEmptyPassword_ReturnTokenWithStatus422(){

        String email = "joao@email.com";
        String password =  "";

        ApplicationError responseBody = testClient
                .post()
                .uri("/api/v1/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ApplicationError.class)
                .returnResult()
                .getResponseBody();

        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getCode()).isEqualTo(422);

    }

}
