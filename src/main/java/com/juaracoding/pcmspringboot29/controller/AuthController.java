package com.juaracoding.pcmspringboot29.controller;

import com.juaracoding.pcmspringboot29.dto.validasi.LoginDTO;
import com.juaracoding.pcmspringboot29.dto.validasi.RegisDTO;
import com.juaracoding.pcmspringboot29.dto.validasi.VerifyRegisDTO;
import com.juaracoding.pcmspringboot29.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
public class AuthController {
    //login
    //regis
    //verifyRegis
    //emailLupaPassword
    //verifyEmailLupaPassword
    //confirmNewPassword
    @Autowired
    AuthService authService;

    @Operation(summary = "User Registration", description = "Registers a new user in the system.")
    @ApiResponse(responseCode = "200", description = "Registration successful")
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping("/regis")
    public ResponseEntity<Object> registration(@Valid @RequestBody RegisDTO regisDTO
            , HttpServletRequest request

    ){
        return authService.regis(authService.mapToUser(regisDTO),request);
    }

    @Operation(summary = "Verify Registration", description = "Verifies a user's registration using a verification token.")
    @ApiResponse(responseCode = "200", description = "Verification successful")
    @ApiResponse(responseCode = "400", description = "Invalid token or user already verified")
    @PostMapping("/verify-regis")
    public ResponseEntity<Object> verifyRegis(@Valid @RequestBody VerifyRegisDTO verifyRegisDTO
            , HttpServletRequest request){
        return authService.verifyRegis(authService.mapToUser(verifyRegisDTO),request);
    }

    @Operation(summary = "User Login", description = "Authenticates a user and returns an authentication token.")
    @ApiResponse(responseCode = "200", description = "Login successful, returns JWT token")
    @ApiResponse(responseCode = "401", description = "Invalid credentials")
    @PostMapping("/login")
    public ResponseEntity<Object> login(@Valid @RequestBody LoginDTO loginDTO
            , HttpServletRequest request){
        return authService.login(authService.mapToUser(loginDTO),request);
    }
}