package com.aws.backend.dto;

import lombok.Data;

@Data
public class UserLogoutRequest {
	private String accessToken;
}
