package com.solace.battleship.events;

/**
 * An object representing a NextNumberChooseRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class NextNumberChooseRequest {
    private String sessionId;

    public String getSessionId() {
        return sessionId;
    }

    public NextNumberChooseRequest() {
    }

    public NextNumberChooseRequest(String sessionId) {
        this.sessionId = sessionId;
    }
}
