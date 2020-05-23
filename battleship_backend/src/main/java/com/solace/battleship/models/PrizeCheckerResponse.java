package com.solace.battleship.models;

public enum PrizeCheckerResponse {
    SUCCESS("Success."), ALREADY_TAKEN("Prize has already been taken."),
    FAILURE("You incorrectly submitted a request for this prize. Ticket is invalid now.");

    public final String message;

    private PrizeCheckerResponse(String message) {
        this.message = message;
    }
}