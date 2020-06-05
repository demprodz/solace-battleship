package com.solace.battleship.models;

public class FourCornersPrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private boolean isTaken;

    public FourCornersPrize() {
    }

    public FourCornersPrize(int numPrizes) {
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
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

    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet) {
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
            numClaimedPrizes++;

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