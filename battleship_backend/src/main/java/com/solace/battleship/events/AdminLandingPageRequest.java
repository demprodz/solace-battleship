package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PlayerPageRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class AdminLandingPageRequest {
    private String sessionId;

    public AdminLandingPageRequest() {
    }

    public AdminLandingPageRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public AdminLandingPageRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AdminLandingPageRequest)) {
            return false;
        }
        AdminLandingPageRequest adminLandingPageRequest = (AdminLandingPageRequest) o;
        return Objects.equals(sessionId, adminLandingPageRequest.sessionId);
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
