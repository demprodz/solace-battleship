package com.solace.battleship.events;

import java.util.HashMap;
import java.util.Objects;

import com.solace.battleship.models.IPrize;

public class SessionSummary {
    private String sessionId;
    private String name;
    private String playerJoinUrl;
    private HashMap<String, Player> players;
    private IPrize[] prizes;
    private boolean isGameInProgress;

    public SessionSummary() {
    }

    public SessionSummary(String sessionId, String name, String playerJoinUrl, HashMap<String, Player> players,
            IPrize[] prizes, boolean isGameInProgress) {
        this.sessionId = sessionId;
        this.name = name;
        this.playerJoinUrl = playerJoinUrl;
        this.players = players;
        this.prizes = prizes;
        this.isGameInProgress = isGameInProgress;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayerJoinUrl() {
        return this.playerJoinUrl;
    }

    public void setPlayerJoinUrl(String playerJoinUrl) {
        this.playerJoinUrl = playerJoinUrl;
    }

    public HashMap<String, Player> getPlayers() {
        return this.players;
    }

    public void setPlayers(HashMap<String, Player> players) {
        this.players = players;
    }

    public IPrize[] getPrizes() {
        return this.prizes;
    }

    public void setPrizes(IPrize[] prizes) {
        this.prizes = prizes;
    }

    public boolean isIsGameInProgress() {
        return this.isGameInProgress;
    }

    public boolean getIsGameInProgress() {
        return this.isGameInProgress;
    }

    public void setIsGameInProgress(boolean isGameInProgress) {
        this.isGameInProgress = isGameInProgress;
    }

    public SessionSummary sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public SessionSummary name(String name) {
        this.name = name;
        return this;
    }

    public SessionSummary playerJoinUrl(String playerJoinUrl) {
        this.playerJoinUrl = playerJoinUrl;
        return this;
    }

    public SessionSummary players(HashMap<String, Player> players) {
        this.players = players;
        return this;
    }

    public SessionSummary prizes(IPrize[] prizes) {
        this.prizes = prizes;
        return this;
    }

    public SessionSummary isGameInProgress(boolean isGameInProgress) {
        this.isGameInProgress = isGameInProgress;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof SessionSummary)) {
            return false;
        }
        SessionSummary sessionSummary = (SessionSummary) o;
        return Objects.equals(sessionId, sessionSummary.sessionId) && Objects.equals(name, sessionSummary.name)
                && Objects.equals(playerJoinUrl, sessionSummary.playerJoinUrl)
                && Objects.equals(players, sessionSummary.players) && Objects.equals(prizes, sessionSummary.prizes)
                && isGameInProgress == sessionSummary.isGameInProgress;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, name, playerJoinUrl, players, prizes, isGameInProgress);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", name='" + getName() + "'" + ", playerJoinUrl='"
                + getPlayerJoinUrl() + "'" + ", players='" + getPlayers() + "'" + ", prizes='" + getPrizes() + "'"
                + ", isGameInProgress='" + isIsGameInProgress() + "'" + "}";
    }

}