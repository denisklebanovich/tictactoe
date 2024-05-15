package com.aws.backend.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/game")
public class GameController {
	private final GameService gameService;


	@GetMapping(value = "/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public SseEmitter subscribe() {
		var username = gameService.getUsername();
		log.info("User {} subscribed", username);
		SseEmitter emitter = gameService.createEmitterForUser(username);
		gameService.tryStartGame();
		return emitter;
	}


	@PostMapping("/move/{gameId}")
	public ResponseEntity<?> move(@PathVariable long gameId, @RequestBody GameService.MoveRequest request) {
		String username = gameService.getUsername();
		log.info("Move request from user {}: {}", username, request);
		return gameService.makeMove(gameId, request, username);
	}
}
