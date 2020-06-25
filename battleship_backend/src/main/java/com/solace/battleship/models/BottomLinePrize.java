package com.solace.battleship.models;

import java.util.ArrayList;

import com.solace.battleship.events.Player;

public class BottomLinePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private ArrayList<String> winners;
    private boolean isTaken;
    private boolean isEnabled;

    public BottomLinePrize() {
    }

    public BottomLinePrize(int numPrizes) {
        isEnabled = true;
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
        this.winners = new ArrayList<String>();
    }

    public String getPrizeName() {
        return "Bottom Line";
    }

    public String getAbbreviatedName() {
        return "BL";
    }

    public String getDescription() {
        return "The ticket with all the numbers of the bottom row marked.";
    }

    public boolean checkPatternMatch(Player player, Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int column = 0; column < spots[0].length; column++) {
            if (spots[2][column] != null && spots[2][column].getIsMarked()
                    && numberSet.isNumberMarked(spots[2][column].getValue())) {
                markedSpots++;
            }
        }

        if (markedSpots == 5) {
            return true;
        }

        return false;
    }

    public void addWinner(String playerName) {
        numClaimedPrizes++;
        winners.add(playerName);

        if (numPrizes == numClaimedPrizes) {
            isTaken = true;
        }
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

    public BottomLinePrize(int numPrizes, int numClaimedPrizes, ArrayList<String> winners, boolean isTaken) {
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

    public BottomLinePrize numPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
        return this;
    }

    public BottomLinePrize numClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
        return this;
    }

    public BottomLinePrize winners(ArrayList<String> winners) {
        this.winners = winners;
        return this;
    }

    public BottomLinePrize isTaken(boolean isTaken) {
        this.isTaken = isTaken;
        return this;
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
        BottomLinePrize bottomLinePrize = (BottomLinePrize) o;
        return numPrizes == bottomLinePrize.numPrizes && isTaken == bottomLinePrize.isTaken;
    }
}