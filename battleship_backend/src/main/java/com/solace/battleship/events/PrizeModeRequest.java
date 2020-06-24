package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PlayerPageRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PrizeModeRequest {
    private String sessionId;
    private String playerId;

    public PrizeModeRequest() {
    }

    public PrizeModeRequest(String sessionId, String playerId) {
        this.sessionId = sessionId;
        this.playerId = playerId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public PrizeModeRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public PrizeModeRequest playerId(String playerId) {
        this.playerId = playerId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PrizeModeRequest)) {
            return false;
        }
        PrizeModeRequest prizeModeRequest = (PrizeModeRequest) o;
        return Objects.equals(sessionId, prizeModeRequest.sessionId)
                && Objects.equals(playerId, prizeModeRequest.playerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, playerId);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", playerId='" + getPlayerId() + "'" + "}";
    }

}
