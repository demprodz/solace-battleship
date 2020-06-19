package com.solace.battleship.events;

import java.util.HashMap;
import java.util.Objects;
import com.solace.battleship.models.GameNumberSet;

/**
 * A GameStart Event
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class GameStart {
    private String sessionId;
    private HashMap<String, Player> players;
    private GameNumberSet gameNumberSet;
    private boolean isAutoMode;
    private int timer;
    private boolean success;

    public GameStart() {
    }

    public GameStart(String sessionId) {
        this.sessionId = sessionId;
        players = new HashMap<String, Player>();
        success = false;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setGameNumberSet(GameNumberSet gameNumberSet) {
        this.gameNumberSet = gameNumberSet;
    }

    public GameNumberSet getGameNumberSet() {
        return gameNumberSet;
    }

    public GameStart(String sessionId, HashMap<String, Player> players, GameNumberSet gameNumberSet, boolean isAutoMode,
            int timer, boolean success) {
        this.sessionId = sessionId;
        this.players = players;
        this.gameNumberSet = gameNumberSet;
        this.isAutoMode = isAutoMode;
        this.timer = timer;
        this.success = success;
    }

    public boolean isIsAutoMode() {
        return this.isAutoMode;
    }

    public boolean getIsAutoMode() {
        return this.isAutoMode;
    }

    public void setIsAutoMode(boolean isAutoMode) {
        this.isAutoMode = isAutoMode;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public GameStart sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public GameStart players(HashMap<String, Player> players) {
        this.players = players;
        return this;
    }

    public GameStart gameNumberSet(GameNumberSet gameNumberSet) {
        this.gameNumberSet = gameNumberSet;
        return this;
    }

    public GameStart isAutoMode(boolean isAutoMode) {
        this.isAutoMode = isAutoMode;
        return this;
    }

    public GameStart timer(int timer) {
        this.timer = timer;
        return this;
    }

    public GameStart success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GameStart)) {
            return false;
        }
        GameStart gameStart = (GameStart) o;
        return Objects.equals(this.players, gameStart.players);
    }

    @Override
    public int hashCode() {
        return Objects.hash(players);
    }

    @Override
    public String toString() {
        return "{" + " Players='" + players + "'}";
    }

}
