package com.aws.backend.dto;

import lombok.Data;

@Data
public class UserConfirmationRequest {
	private String username;
	private String confirmationCode;
}
