package com.organiza.api.modules.users.infra.http.controllers;


import com.organiza.api.http.exception.ApplicationError;
import com.organiza.api.jwt.JwtToken;
import com.organiza.api.jwt.JwtUserDetailsService;
import com.organiza.api.modules.users.domain.dtos.UserAuthenticationDto;
import com.organiza.api.modules.users.domain.dtos.UserResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "Resource that provides authentication in API.")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CreateSessionController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Authenticate in API", description = "Resource for authenticate in API.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "authentication created successfully.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "422", description = "email or password invalid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid credentials.",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ApplicationError.class)))
            })
    @PostMapping("/auth")
    public ResponseEntity<?> authenticate(@RequestBody @Valid UserAuthenticationDto body, HttpServletRequest request) {
        log.info("Authentication process starting with user {}", body.getEmail());
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(body.getEmail(), body.getPassword());

            authenticationManager.authenticate(authenticationToken);
            JwtToken token = detailsService.getTokenAuthenticated(body.getEmail());

            return ResponseEntity.ok(token);

        } catch (AuthenticationException e) {
            log.warn("Bad credentials from user {}", body.getEmail());
        }
        return ResponseEntity
                .badRequest()
                .body(new ApplicationError(request, HttpStatus.BAD_REQUEST, "invalid credentials. Try again."));



    }

}
