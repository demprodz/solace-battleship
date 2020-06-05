package com.solace.battleship.events;

/**
 * An object representing a PlayerPageRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PlayerPageRequest {
    private String sessionId;
    private String playerId;

    public PlayerPageRequest() {
    }

    public PlayerPageRequest(String sessionId, String playerId) {
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

}
