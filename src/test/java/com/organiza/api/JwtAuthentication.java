package com.organiza.api;


import com.organiza.api.jwt.JwtToken;
import com.organiza.api.modules.users.domain.dtos.UserAuthenticationDto;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.Objects;
import java.util.function.Consumer;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderJwtToken(WebTestClient client, String email, String password){
        String token = Objects.requireNonNull(client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UserAuthenticationDto(email, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody()).getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }

}
