package com.solace.battleship.models;

import java.util.ArrayList;

public class TicketSet {
    private ArrayList<Ticket> tickets;

    public TicketSet() {
    }

    public void setTickets(ArrayList<Ticket> tickets) {
        this.tickets = tickets;
    }

    public ArrayList<Ticket> getTickets() {
        return this.tickets;
    }

    public Ticket getTicket(int ticketNumber) {
        return this.tickets.get(ticketNumber);
    }
}