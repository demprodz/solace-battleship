package com.solace.battleship.models;

import java.util.ArrayList;
import java.util.Objects;

import com.solace.battleship.events.Player;

public class EarlyFivePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private ArrayList<String> winners;
    private boolean isTaken;
    private boolean isEnabled;

    public EarlyFivePrize() {
    }

    public EarlyFivePrize(int numPrizes) {
        this.isEnabled = true;
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
        this.winners = new ArrayList<String>();
    }

    public String getPrizeName() {
        return "Early Five";
    }

    public String getAbbreviatedName() {
        return "EF";
    }

    public String getDescription() {
        return "The ticket with five numbers marked.";
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

        if (markedSpots >= 5) {
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

    public int getNumClaimedPrizes() {
        return this.numClaimedPrizes;
    }

    public boolean getIsTaken() {
        return this.isTaken;
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

    public EarlyFivePrize(int numPrizes, int numClaimedPrizes, ArrayList<String> winners, boolean isTaken) {
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

    public EarlyFivePrize numPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
        return this;
    }

    public EarlyFivePrize numClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
        return this;
    }

    public EarlyFivePrize winners(ArrayList<String> winners) {
        this.winners = winners;
        return this;
    }

    public EarlyFivePrize isTaken(boolean isTaken) {
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
        EarlyFivePrize earlyFivePrize = (EarlyFivePrize) o;
        return numPrizes == earlyFivePrize.numPrizes && isTaken == earlyFivePrize.isTaken;
    }
}