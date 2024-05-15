package com.aws.backend.controller;

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType;
import com.aws.backend.dto.RefreshTokenRequest;
import com.aws.backend.dto.UserConfirmationRequest;
import com.aws.backend.dto.UserLogoutRequest;
import com.aws.backend.dto.UserRegistrationRequest;
import com.aws.backend.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthenticationService authenticationService;


	@PostMapping("/register")
	public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
		return authenticationService.registerUser(request.getUsername(), request.getPassword(), request.getEmail());
	}

	@PostMapping("/confirm")
	public ResponseEntity<String> confirmUser(@RequestBody UserConfirmationRequest request) {
		return authenticationService.confirmUser(request.getUsername(), request.getConfirmationCode());
	}

	@PostMapping("/login")
	public AuthenticationResultType login(@RequestBody UserRegistrationRequest request) {
		return authenticationService.login(request.getUsername(), request.getPassword());
	}

	@PostMapping("/logout")
	public void logout(@RequestBody UserLogoutRequest request) {
		authenticationService.signOut(request.getAccessToken());
	}

	@PostMapping("/refresh")
	public AuthenticationResultType refreshToken(@RequestBody RefreshTokenRequest request) {
		return authenticationService.refreshToken(request.getRefreshToken());
	}
}
