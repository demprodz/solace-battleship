package com.solace.battleship.events;

import java.util.Objects;

import com.solace.battleship.models.PrizeCheckerResponse;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PrizeSubmitResult {
    private String sessionId;
    private String playerId;
    private String playerName;
    private int ticket;
    private int selectedPrizeIndex;
    private boolean success;
    private String returnMessage;
    private PrizeCheckerResponse responseType;

    public PrizeSubmitResult() {
    }

    public PrizeSubmitResult(String sessionId, String playerId, String playerName, int ticket, int selectedPrizeIndex,
            boolean success, String returnMessage, PrizeCheckerResponse responseType) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.playerName = playerName;
        this.ticket = ticket;
        this.selectedPrizeIndex = selectedPrizeIndex;
        this.success = success;
        this.returnMessage = returnMessage;
        this.responseType = responseType;
    }

    public PrizeSubmitResult(String sessionId, String playerId, boolean success, String returnMessage) {
        this.sessionId = sessionId;
        this.playerId = playerId;
        this.success = success;
        this.returnMessage = returnMessage;
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

    public String getPlayerName() {
        return this.playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
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

    public boolean isSuccess() {
        return this.success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReturnMessage() {
        return this.returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public PrizeCheckerResponse getResponseType() {
        return this.responseType;
    }

    public void setResponseType(PrizeCheckerResponse responseType) {
        this.responseType = responseType;
    }

    public PrizeSubmitResult sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public PrizeSubmitResult playerId(String playerId) {
        this.playerId = playerId;
        return this;
    }

    public PrizeSubmitResult playerName(String playerName) {
        this.playerName = playerName;
        return this;
    }

    public PrizeSubmitResult ticket(int ticket) {
        this.ticket = ticket;
        return this;
    }

    public PrizeSubmitResult selectedPrizeIndex(int selectedPrizeIndex) {
        this.selectedPrizeIndex = selectedPrizeIndex;
        return this;
    }

    public PrizeSubmitResult success(boolean success) {
        this.success = success;
        return this;
    }

    public PrizeSubmitResult returnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
        return this;
    }

    public PrizeSubmitResult responseType(PrizeCheckerResponse responseType) {
        this.responseType = responseType;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PrizeSubmitResult)) {
            return false;
        }
        PrizeSubmitResult prizeSubmitResult = (PrizeSubmitResult) o;
        return Objects.equals(sessionId, prizeSubmitResult.sessionId)
                && Objects.equals(playerId, prizeSubmitResult.playerId)
                && Objects.equals(playerName, prizeSubmitResult.playerName) && ticket == prizeSubmitResult.ticket
                && selectedPrizeIndex == prizeSubmitResult.selectedPrizeIndex && success == prizeSubmitResult.success
                && Objects.equals(returnMessage, prizeSubmitResult.returnMessage)
                && Objects.equals(responseType, prizeSubmitResult.responseType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, playerId, playerName, ticket, selectedPrizeIndex, success, returnMessage,
                responseType);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", playerId='" + getPlayerId() + "'" + ", playerName='"
                + getPlayerName() + "'" + ", ticket='" + getTicket() + "'" + ", selectedPrizeIndex='"
                + getSelectedPrizeIndex() + "'" + ", success='" + isSuccess() + "'" + ", returnMessage='"
                + getReturnMessage() + "'" + ", responseType='" + getResponseType() + "'" + "}";
    }

}
