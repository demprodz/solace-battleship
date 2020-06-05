package com.solace.battleship.events;

import com.solace.battleship.models.TicketSet;

/**
 * An object representing a player and its associated states
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class Player {
    private String id;
    private String name;
    private String sessionId;
    private TicketSet ticketSet;
    private int numTickets;

    public Player(String sessionId, String id, String name, TicketSet ticketSet, int numTickets) {
        this.sessionId = sessionId;
        this.id = id;
        this.name = name;
        this.ticketSet = ticketSet;
        this.numTickets = numTickets;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getNumTickets() {
        return this.numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public Player sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public Player numTickets(int numTickets) {
        this.numTickets = numTickets;
        return this;
    }

    public TicketSet getTicketSet() {
        return ticketSet;
    }

    public void setTicketSet(TicketSet ticketSet) {
        this.ticketSet = ticketSet;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Player id(String id) {
        this.id = id;
        return this;
    }

    public Player name(String name) {
        this.name = name;
        return this;
    }

    public Player ticketSet(TicketSet ticketSet) {
        this.ticketSet = ticketSet;
        return this;
    }
}
