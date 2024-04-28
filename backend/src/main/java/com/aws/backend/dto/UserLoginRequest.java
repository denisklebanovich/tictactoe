package com.aws.backend.dto;

import lombok.Data;

@Data
public class UserLoginRequest {
	private String username;
	private String password;
}
