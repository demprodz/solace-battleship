package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class SessionCreateResult {
    private SessionSummary sessionSummary;
    private boolean success;

    public SessionCreateResult() {
        this.success = false;
    }

    public SessionCreateResult(SessionSummary sessionSummary, boolean success) {
        this.sessionSummary = sessionSummary;
        this.success = success;
    }

    public SessionSummary getSessionSummary() {
        return this.sessionSummary;
    }

    public void setSessionSummary(SessionSummary sessionSummary) {
        this.sessionSummary = sessionSummary;
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

    public SessionCreateResult sessionSummary(SessionSummary sessionSummary) {
        this.sessionSummary = sessionSummary;
        return this;
    }

    public SessionCreateResult success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SessionCreateResult)) {
            return false;
        }
        SessionCreateResult sessionCreateResult = (SessionCreateResult) o;
        return Objects.equals(sessionSummary, sessionCreateResult.sessionSummary)
                && success == sessionCreateResult.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionSummary, success);
    }

    @Override
    public String toString() {
        return "{" + " sessionSummary='" + getSessionSummary() + "'" + ", success='" + isSuccess() + "'" + "}";
    }

}
