package com.solace.battleship.events;

import java.util.Objects;

public class SessionSearchRequest {
    private String sessionId;

    public SessionSearchRequest() {
    }

    public SessionSearchRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public SessionSearchRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SessionSearchRequest)) {
            return false;
        }
        SessionSearchRequest sessionSearchRequest = (SessionSearchRequest) o;
        return Objects.equals(sessionId, sessionSearchRequest.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(sessionId);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + "}";
    }

}