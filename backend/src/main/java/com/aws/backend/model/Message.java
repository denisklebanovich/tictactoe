package com.aws.backend.model;

public interface Message {
	String getType();
	String getGameId();
	String getContent();
}
