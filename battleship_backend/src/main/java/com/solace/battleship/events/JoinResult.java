package com.solace.battleship.events;

import java.util.Objects;

import com.solace.battleship.models.TicketSet;

/**
 * The result of a JoinRequest
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class JoinResult {

    private String playerId;
    private String playerNickname;
    private boolean success;
    private String message;
    private TicketSet ticketSet;

    public JoinResult(final String playerId, final String playerNickname, final boolean success, final String message,
            final TicketSet ticketSet) {
        this.playerId = playerId;
        this.playerNickname = playerNickname;
        this.success = success;
        this.message = message;
        this.ticketSet = ticketSet;
    }

    public TicketSet getTicketSet() {
        return ticketSet;
    }

    public void setTicketSet(final TicketSet ticketSet) {
        this.ticketSet = ticketSet;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(final String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerNickname() {
        return playerNickname;
    }

    public void setPlayerNickname(final String playerNickname) {
        this.playerNickname = playerNickname;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(final boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public JoinResult success(boolean success) {
        this.success = success;
        return this;
    }

    public JoinResult message(String message) {
        this.message = message;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof JoinResult)) {
            return false;
        }
        JoinResult joinResult = (JoinResult) o;
        return Objects.equals(playerId, joinResult.playerId)
                && Objects.equals(playerNickname, joinResult.playerNickname) && success == joinResult.success
                && Objects.equals(message, joinResult.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, playerNickname, success, message);
    }

    @Override
    public String toString() {
        return "{" + "playerId='" + getPlayerId() + "'" + ", playerNickname='" + getPlayerNickname() + "'"
                + ", success='" + isSuccess() + "'" + ", message='" + getMessage() + "'" + "}";
    }

}
