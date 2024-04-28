package com.aws.backend.configuration;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider;
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CognitoConfiguration {
	@Bean
	AWSCognitoIdentityProvider cognitoIdentityProvider() {
		return AWSCognitoIdentityProviderClientBuilder
				.standard()
				.withRegion(Regions.US_EAST_1)
				.build();
	}
}
