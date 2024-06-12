package com.aws.backend.sse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();
    private final Set<String> waitingPlayers = ConcurrentHashMap.newKeySet();
    private final Map<Long, Game> games = new ConcurrentHashMap<>();
    private final GameResultRepository gameResultRepository;


    public List<GameResult> getResults() {
        return gameResultRepository.findAll();
    }

    public SseEmitter createEmitterForUser(String username) {
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);
        this.emitters.put(username, emitter);
        this.waitingPlayers.add(username);
        setupEmitterCleanup(emitter, username);
        log.info("User {} added to emitters and waitingPlayers", username);
        return emitter;
    }

    private void setupEmitterCleanup(SseEmitter emitter, String username) {
        Runnable removeUser = () -> {
            this.emitters.remove(username);
            this.waitingPlayers.remove(username);
            log.info("{} removed from emitters and waitingPlayers", username);
        };
        emitter.onCompletion(removeUser);
        emitter.onTimeout(removeUser);
        emitter.onError((e) -> removeUser.run());
    }

    public void tryStartGame() {
        if (this.waitingPlayers.size() >= 2) {
            Iterator<String> iterator = this.waitingPlayers.iterator();
            String player1 = iterator.next();
            iterator.remove();
            String player2 = iterator.next();
            iterator.remove();

            Game game = new Game(player1, player2, gameEvent -> {
                var firstEmitter = emitters.get(player1);
                var secondEmitter = emitters.get(player2);

                if (gameEvent.event.equals(GameStatus.GAME_ENDED)) {
                    var winner = gameEvent.winner();
                    var gameResult = new GameResult(player1, player2, winner);
                    gameResultRepository.save(gameResult);
                }
                try {
                    firstEmitter.send(gameEvent);
                    secondEmitter.send(gameEvent);
                } catch (IOException e) {
                    log.info("Something bad happened while emitting events");
                }
            });
            this.games.put(game.getGameId(), game);
        }
    }

    public ResponseEntity<?> makeMove(long gameId, MoveRequest request, String username) {
        Game game = this.games.get(gameId);
        if (game == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse("Game not found"));
        }
        try {
            game.makeMove(request.y(), request.x(), username);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            log.error("Illegal move by {}: {}", username, e.getMessage());
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    public String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        var jwt = ((Jwt) authentication.getPrincipal());
        return jwt.getClaim("username");
    }

    public record GameEvent(String firstPlayer, String secondPlayer,
                            GameStatus event,
                            Game.Cell[][] board,
                            String winner, String currentPlayer, long gameId) {
    }

    public record MoveRequest(int x, int y) {
    }

    public enum GameStatus {GAME_STARTED, GAME_UPDATED, GAME_ENDED, ILLEGAL_MOVE}

    public record ErrorResponse(String message) {
    }
}
