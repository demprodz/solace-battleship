package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PlayerPageRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class SessionCreateRequest {
    private String name;
    private String sessionId;
    private String playerJoinUrl;

    public SessionCreateRequest() {
    }

    public SessionCreateRequest(String name, String sessionId, String playerJoinUrl) {
        this.name = name;
        this.sessionId = sessionId;
        this.playerJoinUrl = playerJoinUrl;
    }

    public String getPlayerJoinUrl() {
        return this.playerJoinUrl;
    }

    public void setPlayerJoinUrl(String playerJoinUrl) {
        this.playerJoinUrl = playerJoinUrl;
    }

    public SessionCreateRequest playerJoinUrl(String playerJoinUrl) {
        this.playerJoinUrl = playerJoinUrl;
        return this;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionCreateRequest name(String name) {
        this.name = name;
        return this;
    }

    public SessionCreateRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SessionCreateRequest)) {
            return false;
        }
        SessionCreateRequest sessionCreateRequest = (SessionCreateRequest) o;
        return Objects.equals(name, sessionCreateRequest.name)
                && Objects.equals(sessionId, sessionCreateRequest.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, sessionId);
    }

    @Override
    public String toString() {
        return "{" + " name='" + getName() + "'" + ", sessionId='" + getSessionId() + "'" + "}";
    }

}
