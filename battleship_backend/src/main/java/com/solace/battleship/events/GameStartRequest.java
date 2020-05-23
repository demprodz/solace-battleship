package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class GameStartRequest {
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public GameStartRequest() {
    }

    public GameStartRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GameStartRequest)) {
            return false;
        }
        GameStartRequest gameStartRequest = (GameStartRequest) o;
        return Objects.equals(sessionId, gameStartRequest.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "{'sessionId='" + getSessionId() + "'" + "}";
    }

}
