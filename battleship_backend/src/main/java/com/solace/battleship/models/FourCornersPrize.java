package com.solace.battleship.models;

import java.util.ArrayList;
import java.util.Objects;

import com.solace.battleship.events.Player;

public class FourCornersPrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private ArrayList<String> winners;
    private boolean isTaken;
    private boolean isEnabled;

    public FourCornersPrize() {
    }

    public FourCornersPrize(int numPrizes) {
        this.isEnabled = true;
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
        this.winners = new ArrayList<String>();
    }

    public String getPrizeName() {
        return "Four Corners";
    }

    public String getAbbreviatedName() {
        return "FC";
    }

    public String getDescription() {
        return "The ticket with all four corners marked i.e. 1st and last numbers of top and bottom rows.";
    }

    public boolean checkPatternMatch(Player player, Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        boolean[][] markedSpots = new boolean[3][5];

        for (int row = 0; row < spots.length; row++) {
            int markedSpotsIndex = 0;
            for (int column = 0; column < spots[0].length; column++) {
                if (spots[row][column] != null && spots[row][column].value > 0) {
                    markedSpots[row][markedSpotsIndex++] = spots[row][column].isMarked
                            && numberSet.isNumberMarked(spots[row][column].getValue());
                }
            }
        }

        if (markedSpots[0][0] && markedSpots[0][4] && markedSpots[2][0] && markedSpots[2][4]) {
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

    public FourCornersPrize(int numPrizes, int numClaimedPrizes, ArrayList<String> winners, boolean isTaken) {
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

    public FourCornersPrize numPrizes(int numPrizes) {
        this.numPrizes = numPrizes;
        return this;
    }

    public FourCornersPrize numClaimedPrizes(int numClaimedPrizes) {
        this.numClaimedPrizes = numClaimedPrizes;
        return this;
    }

    public FourCornersPrize winners(ArrayList<String> winners) {
        this.winners = winners;
        return this;
    }

    public FourCornersPrize isTaken(boolean isTaken) {
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
        FourCornersPrize fourCornersPrize = (FourCornersPrize) o;
        return numPrizes == fourCornersPrize.numPrizes && isTaken == fourCornersPrize.isTaken;
    }
}