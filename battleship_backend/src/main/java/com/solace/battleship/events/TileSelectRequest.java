package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class TileSelectRequest {
    private String sessionId;
    private String playerId;
    private int ticket;
    private int row;
    private int column;

    public TileSelectRequest() {
    }

    public TileSelectRequest(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getTicket() {
        return ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof TileSelectRequest)) {
            return false;
        }
        TileSelectRequest tileSelectRequest = (TileSelectRequest) o;
        return Objects.equals(sessionId, tileSelectRequest.sessionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId);
    }

    @Override
    public String toString() {
        return "{'sessionId='" + getSessionId() + "'" + "}";
    }

}
