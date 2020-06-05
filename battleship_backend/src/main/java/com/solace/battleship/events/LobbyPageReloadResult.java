package com.solace.battleship.events;

import java.util.Objects;
import java.util.Map;

/**
 * An object representing a player and its associated states
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class LobbyPageReloadResult {
    private Map<String, SessionSummary> sessions;
    private boolean success;

    public LobbyPageReloadResult() {
        this.success = false;
    }

    public LobbyPageReloadResult(Map<String, SessionSummary> sessions, boolean success) {
        this.sessions = sessions;
        this.success = success;
    }

    public Map<String, SessionSummary> getSessions() {
        return this.sessions;
    }

    public void setSessions(Map<String, SessionSummary> sessions) {
        this.sessions = sessions;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public LobbyPageReloadResult sessions(Map<String, SessionSummary> sessions) {
        this.sessions = sessions;
        return this;
    }

    public LobbyPageReloadResult success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof LobbyPageReloadResult)) {
            return false;
        }
        LobbyPageReloadResult lobbyPageReloadResult = (LobbyPageReloadResult) o;
        return Objects.equals(sessions, lobbyPageReloadResult.sessions) && success == lobbyPageReloadResult.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessions, success);
    }

    @Override
    public String toString() {
        return "{" + " sessions='" + getSessions() + "'" + ", success='" + isSuccess() + "'" + "}";
    }

}
