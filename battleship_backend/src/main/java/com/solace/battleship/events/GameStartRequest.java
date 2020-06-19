package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class GameStartRequest {
    private String sessionId;
    private boolean isAutoMode;
    private String selectedTimer;
    private int[] disabledPrizesIndexList;

    public GameStartRequest(String sessionId, boolean isAutoMode, String selectedTimer, int[] disabledPrizesIndexList) {
        this.sessionId = sessionId;
        this.isAutoMode = isAutoMode;
        this.selectedTimer = selectedTimer;
        this.disabledPrizesIndexList = disabledPrizesIndexList;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public boolean isIsAutoMode() {
        return this.isAutoMode;
    }

    public boolean getIsAutoMode() {
        return this.isAutoMode;
    }

    public void setIsAutoMode(boolean isAutoMode) {
        this.isAutoMode = isAutoMode;
    }

    public String getSelectedTimer() {
        return this.selectedTimer;
    }

    public void setSelectedTimer(String selectedTimer) {
        this.selectedTimer = selectedTimer;
    }

    public GameStartRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public GameStartRequest isAutoMode(boolean isAutoMode) {
        this.isAutoMode = isAutoMode;
        return this;
    }

    public GameStartRequest selectedTimer(String selectedTimer) {
        this.selectedTimer = selectedTimer;
        return this;
    }

    public String getSessionId() {
        return sessionId;
    }

    public GameStartRequest() {
    }

    public GameStartRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public int[] getDisabledPrizesIndexList() {
        return this.disabledPrizesIndexList;
    }

    public void setDisabledPrizesIndexList(int[] disabledPrizesIndexList) {
        this.disabledPrizesIndexList = disabledPrizesIndexList;
    }

    public GameStartRequest disabledPrizesIndexList(int[] disabledPrizesIndexList) {
        this.disabledPrizesIndexList = disabledPrizesIndexList;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof GameStartRequest)) {
            return false;
        }
        GameStartRequest gameStartRequest = (GameStartRequest) o;
        return Objects.equals(sessionId, gameStartRequest.sessionId) && isAutoMode == gameStartRequest.isAutoMode
                && Objects.equals(selectedTimer, gameStartRequest.selectedTimer)
                && Objects.equals(disabledPrizesIndexList, gameStartRequest.disabledPrizesIndexList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, isAutoMode, selectedTimer, disabledPrizesIndexList);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", isAutoMode='" + isIsAutoMode() + "'"
                + ", selectedTimer='" + getSelectedTimer() + "'" + ", disabledPrizesIndexList='"
                + getDisabledPrizesIndexList() + "'" + "}";
    }

}
