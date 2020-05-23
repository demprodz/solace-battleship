package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PrizeSubmitRequest {
    private String sessionId;
    private String playerId;
    private int ticket;
    private int selectedPrizeIndex;

    public PrizeSubmitRequest() {
    }

    public PrizeSubmitRequest(String sessionId, String playerId, int ticket, int selectedPrizeIndex) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.ticket = ticket;
        this.selectedPrizeIndex = selectedPrizeIndex;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public int getTicket() {
        return this.ticket;
    }

    public void setTicket(int ticket) {
        this.ticket = ticket;
    }

    public int getSelectedPrizeIndex() {
        return this.selectedPrizeIndex;
    }

    public void setSelectedPrizeIndex(int selectedPrizeIndex) {
        this.selectedPrizeIndex = selectedPrizeIndex;
    }

    public PrizeSubmitRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public PrizeSubmitRequest playerId(String playerId) {
        this.playerId = playerId;
        return this;
    }

    public PrizeSubmitRequest ticket(int ticket) {
        this.ticket = ticket;
        return this;
    }

    public PrizeSubmitRequest selectedPrizeIndex(int selectedPrizeIndex) {
        this.selectedPrizeIndex = selectedPrizeIndex;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PrizeSubmitRequest)) {
            return false;
        }
        PrizeSubmitRequest prizeSubmitRequest = (PrizeSubmitRequest) o;
        return Objects.equals(sessionId, prizeSubmitRequest.sessionId)
                && Objects.equals(playerId, prizeSubmitRequest.playerId) && ticket == prizeSubmitRequest.ticket
                && selectedPrizeIndex == prizeSubmitRequest.selectedPrizeIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, playerId, ticket, selectedPrizeIndex);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", playerId='" + getPlayerId() + "'" + ", ticket='"
                + getTicket() + "'" + ", selectedPrizeIndex='" + getSelectedPrizeIndex() + "'" + "}";
    }

}
