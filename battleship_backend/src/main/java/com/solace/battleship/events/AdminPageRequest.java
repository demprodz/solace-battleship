package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PlayerPageRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class AdminPageRequest {
    private String sessionId;

    public AdminPageRequest() {
    }

    public AdminPageRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AdminPageRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AdminPageRequest)) {
            return false;
        }
        AdminPageRequest adminPageRequest = (AdminPageRequest) o;
        return Objects.equals(sessionId, adminPageRequest.sessionId);
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
