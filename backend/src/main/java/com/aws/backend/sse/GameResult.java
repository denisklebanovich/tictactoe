package com.aws.backend.sse;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class GameResult {
    @Id
    @GeneratedValue
    private Long id;

    private String firstPlayer;
    private String secondPlayer;
    private GameResultStatus status;

    public GameResult(String firstPlayer, String secondPlayer, String winner) {
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        if (winner == null) {
            this.status = GameResultStatus.DRAW;
        } else if (winner.equals(firstPlayer)) {
            this.status = GameResultStatus.FIRST_PLAYER_WON;
        } else {
            this.status = GameResultStatus.SECOND_PLAYER_WON;
        }
    }
}
