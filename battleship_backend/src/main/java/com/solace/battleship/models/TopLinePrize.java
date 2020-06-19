package com.solace.battleship.models;

import java.util.ArrayList;
import java.util.Objects;

import com.solace.battleship.events.Player;

public class TopLinePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private ArrayList<String> winners;
    private boolean isTaken;
    private boolean isEnabled;

    public TopLinePrize() {
    }

    public TopLinePrize(int numPrizes) {
        this.isEnabled = true;
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
        this.winners = new ArrayList<String>();
    }

    public String getPrizeName() {
        return "Top Line";
    }

    public String getAbbreviatedName() {
        return "TL";
    }

    public String getDescription() {
        return "The ticket with all the numbers of the top row marked.";
    }

    public boolean checkPatternMatch(Player player, Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int column = 0; column < spots[0].length; column++) {
            if (spots[0][column] != null && spots[0][column].getIsMarked()
                    && numberSet.isNumberMarked(spots[0][column].getValue())) {
                markedSpots++;
            }
        }

        if (markedSpots == 5) {
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

    public TopLinePrize(int numPrizes, int numClaimedPrizes, ArrayList<String> winners, boolean isTaken) {
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

    public TopLinePrize numPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
        return this;
    }

    public TopLinePrize numClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
        return this;
    }

    public TopLinePrize winners(ArrayList<String> winners) {
        this.winners = winners;
        return this;
    }

    public TopLinePrize isTaken(boolean isTaken) {
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
        TopLinePrize topLinePrize = (TopLinePrize) o;
        return numPrizes == topLinePrize.numPrizes && isTaken == topLinePrize.isTaken;
    }
}