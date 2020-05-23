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
