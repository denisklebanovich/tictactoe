package com.aws.backend.model;

import lombok.Data;

@Data
public class PlayerMessage implements Message {
	private String type;
	private String gameId;
	private String player;
	private String content;
}
