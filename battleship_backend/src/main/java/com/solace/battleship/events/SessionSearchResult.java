package com.solace.battleship.events;

import java.util.Objects;

public class SessionSearchResult {
    private String sessionId;
    private boolean success;
    private String errorMessage;

    public SessionSearchResult() {
    }

    public SessionSearchResult(String sessionId, boolean success) {
        this.sessionId = sessionId;
        this.success = success;
    }

    public SessionSearchResult(String sessionId, String errorMessage) {
        this.sessionId = sessionId;
        this.success = false;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public SessionSearchResult errorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public SessionSearchResult sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionSearchResult success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SessionSearchResult)) {
            return false;
        }
        SessionSearchResult sessionSearchResult = (SessionSearchResult) o;
        return Objects.equals(sessionId, sessionSearchResult.sessionId) && success == sessionSearchResult.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, success);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", success='" + isSuccess() + "'" + "}";
    }

}