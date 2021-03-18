package com.bowling.bowlingapp.game;


/**
 * object to capture frame updates from front end
 *
 * essentially it represents one roll
 */
public class FrameUpdate {

    private Long gameId;
    private String playerName;
    private int rollScore;

    public FrameUpdate() {
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRollScore() {
        return rollScore;
    }

    public void setRollScore(int rollScore) {
        this.rollScore = rollScore;
    }
}
