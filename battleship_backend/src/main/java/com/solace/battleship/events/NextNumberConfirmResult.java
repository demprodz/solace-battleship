package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a NextNumberConfirmResult request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class NextNumberConfirmResult {
    private String sessionId;
    private boolean success;
    private boolean isGameOver;
    private String returnMessage;

    public NextNumberConfirmResult() {
    }

    public NextNumberConfirmResult(String sessionId, boolean success, boolean isGameOver) {
        this.sessionId = sessionId;
        this.success = success;
        this.isGameOver = isGameOver;
    }

    public NextNumberConfirmResult(String sessionId, boolean success, String returnMessage) {
        this.sessionId = sessionId;
        this.success = success;
        this.returnMessage = returnMessage;
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

    public boolean isIsGameOver() {
        return this.isGameOver;
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public String getReturnMessage() {
        return this.returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public NextNumberConfirmResult sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public NextNumberConfirmResult success(boolean success) {
        this.success = success;
        return this;
    }

    public NextNumberConfirmResult isGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
        return this;
    }

    public NextNumberConfirmResult returnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof NextNumberConfirmResult)) {
            return false;
        }
        NextNumberConfirmResult nextNumberConfirmResult = (NextNumberConfirmResult) o;
        return Objects.equals(sessionId, nextNumberConfirmResult.sessionId)
                && success == nextNumberConfirmResult.success && isGameOver == nextNumberConfirmResult.isGameOver
                && Objects.equals(returnMessage, nextNumberConfirmResult.returnMessage);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, success, isGameOver, returnMessage);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", success='" + isSuccess() + "'" + ", isGameOver='"
                + isIsGameOver() + "'" + ", returnMessage='" + getReturnMessage() + "'" + "}";
    }

}
