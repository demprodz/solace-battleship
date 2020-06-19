package com.solace.battleship.models;

import java.util.ArrayList;
import java.util.Objects;

import com.solace.battleship.events.Player;

public class FullHousePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private ArrayList<String> winners;
    private boolean isTaken;
    private boolean isEnabled;

    public FullHousePrize() {
    }

    public FullHousePrize(int numPrizes) {
        this.isEnabled = true;
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
        this.winners = new ArrayList<String>();
    }

    public String getPrizeName() {
        return "Full House";
    }

    public String getAbbreviatedName() {
        return "FH";
    }

    public String getDescription() {
        return "The ticket with all the 15 numbers marked.";
    }

    public boolean checkPatternMatch(Player player, Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int i = 0; i < spots.length; i++) {
            for (int j = 0; j < spots[0].length; j++) {
                if (spots[i][j] != null && spots[i][j].getIsMarked()
                        && numberSet.isNumberMarked(spots[i][j].getValue())) {
                    markedSpots++;
                }
            }
        }

        if (markedSpots == 15) {
            numClaimedPrizes++;
            winners.add(player.getName());

            if (numPrizes == numClaimedPrizes) {
                isTaken = true;
            }

            return true;
        }

        return false;
    }

    public int getNumPrizes() {
        return this.numPrizes;
    }

    public void setNumPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
    }

    public boolean getIsTaken() {
        return this.isTaken;
    }

    public int getNumClaimedPrizes() {
        return this.numClaimedPrizes;
    }

    public void setIsTaken(boolean isTaken) {
        this.isTaken = isTaken;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public FullHousePrize(int numPrizes, int numClaimedPrizes, ArrayList<String> winners, boolean isTaken) {
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = numClaimedPrizes;
        this.winners = winners;
        this.isTaken = isTaken;
    }

    public void setNumClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
    }

    public ArrayList<String> getWinners() {
        return this.winners;
    }

    public void setWinners(ArrayList<String> winners) {
        this.winners = winners;
    }

    public boolean isIsTaken() {
        return this.isTaken;
    }

    public FullHousePrize numPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
        return this;
    }

    public FullHousePrize numClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
        return this;
    }

    public FullHousePrize winners(ArrayList<String> winners) {
        this.winners = winners;
        return this;
    }

    public FullHousePrize isTaken(boolean isTaken) {
        this.isTaken = isTaken;
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numPrizes, numClaimedPrizes, winners, isTaken);
    }

    @Override
    public String toString() {
        return "{" + " numPrizes='" + getNumPrizes() + "'" + ", numClaimedPrizes='" + getNumClaimedPrizes() + "'"
                + ", winners='" + getWinners() + "'" + ", isTaken='" + isIsTaken() + "'" + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof EarlyFivePrize)) {
            return false;
        }
        FullHousePrize fullHousePrize = (FullHousePrize) o;
        return numPrizes == fullHousePrize.numPrizes && isTaken == fullHousePrize.isTaken;
    }
}