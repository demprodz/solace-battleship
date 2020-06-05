package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a player and its associated states
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class AdminLandingPageReloadResult {
    private SessionSummary sessionSummary;
    private boolean success;

    public AdminLandingPageReloadResult() {
        this.success = false;
    }

    public AdminLandingPageReloadResult(SessionSummary sessionSummary, boolean success) {
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

    public AdminLandingPageReloadResult sessionSummary(SessionSummary sessionSummary) {
        this.sessionSummary = sessionSummary;
        return this;
    }

    public AdminLandingPageReloadResult success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AdminLandingPageReloadResult)) {
            return false;
        }
        AdminLandingPageReloadResult adminLandingPageReloadResult = (AdminLandingPageReloadResult) o;
        return Objects.equals(sessionSummary, adminLandingPageReloadResult.sessionSummary)
                && success == adminLandingPageReloadResult.success;
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
