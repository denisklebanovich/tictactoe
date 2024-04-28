package com.aws.backend.service;

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {
	private final AWSCognitoIdentityProvider cognitoIdentityProvider;
	@Value("${cognito.user-client-id}")
	private String clientId;

	public ResponseEntity<String> registerUser(String username, String password, String email) {
		log.info("Registering user with username: {}, email: {}", username, email);
		SignUpRequest request = new SignUpRequest()
				.withClientId(clientId)
				.withUsername(username)
				.withPassword(password)
				.withUserAttributes(
						new AttributeType()
								.withName("email")
								.withValue(email)
				);
		try {
			cognitoIdentityProvider.signUp(request);
			log.info("User {} registered successfully", username);
			return ResponseEntity.ok("User registered successfully");
		} catch (Exception e) {
			log.error("User {} registration failed: {}", username, e.getMessage());
			return ResponseEntity.badRequest().body("User registration failed. Check the password requirements.");
		}
	}

	public ResponseEntity<String> confirmUser(String username, String confirmationCode) {
		log.info("Confirming user with username: {}", username);
		ConfirmSignUpRequest confirmSignUpRequest = new ConfirmSignUpRequest()
				.withClientId(clientId)
				.withUsername(username)
				.withConfirmationCode(confirmationCode);
		try {
			cognitoIdentityProvider.confirmSignUp(confirmSignUpRequest);
			log.info("User {} confirmed successfully", username);
			return ResponseEntity.ok("User confirmed successfully");
		} catch (Exception e) {
			log.error("User confirmation failed: {}", e.getMessage());
			return ResponseEntity.badRequest().body("User confirmation failed: " + e.getMessage());
		}
	}

	public AuthenticationResultType login(String username, String password) {
		log.info("Logging in user with username: {}", username);
		InitiateAuthRequest authRequest = new InitiateAuthRequest()
				.withClientId(clientId)
				.withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.addAuthParametersEntry("USERNAME", username)
				.addAuthParametersEntry("PASSWORD", password);
		try {
			var authResponse = cognitoIdentityProvider.initiateAuth(authRequest);
			var result = authResponse.getAuthenticationResult();
			log.info("User {} logged in successfully", username);
			return result;
		} catch (NotAuthorizedException e) {
			log.error("User login failed: {}", e.getErrorMessage());
		}
		return null;
	}
}


