package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PlayerJoin request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PlayerJoined {
    private String playerId;
    private PlayerName playerName;
    private String playerNickname;
    private String sessionId;
    private int numTickets;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public PlayerName getPlayerName() {
        return playerName;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setPlayerName(final PlayerName playerName) {
        this.playerName = playerName;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public void setPlayerNickname(final String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(final int numTickets) {
        this.numTickets = numTickets;
    }

    public PlayerJoined() {
    }

    public PlayerJoined(PlayerName playerName, String playerNickname) {
        this.playerName = playerName;
        this.playerNickname = playerNickname;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PlayerJoined)) {
            return false;
        }
        PlayerJoined playerJoined = (PlayerJoined) o;
        return Objects.equals(playerName, playerJoined.playerName)
                && Objects.equals(playerNickname, playerJoined.playerNickname)
                && Objects.equals(sessionId, playerJoined.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerName, playerNickname, sessionId);
    }

    @Override
    public String toString() {
        return "{" + " playerName='" + getPlayerName() + "'" + ", playerNickname='" + getPlayerNickname() + "'"
                + ", sessionId='" + getSessionId() + "'" + "}";
    }

}
