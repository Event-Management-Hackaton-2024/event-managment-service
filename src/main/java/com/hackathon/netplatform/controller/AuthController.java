package com.hackathon.netplatform.controller;

import com.hackathon.netplatform.dto.request.AuthRequestDto;
import com.hackathon.netplatform.dto.request.UserRequestDto;
import com.hackathon.netplatform.dto.response.AuthResponseDto;
import com.hackathon.netplatform.dto.response.UserResponseDto;
import com.hackathon.netplatform.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register user")
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto register(
            @Valid @RequestBody UserRequestDto userRequestDto) {
        return authService.registerUser(userRequestDto);
    }

    @Operation(summary = "Login")
    @PostMapping("/login")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponseDto login(@Valid @RequestBody AuthRequestDto authRequestDto) {
        return authService.authenticate(authRequestDto);
    }
}
