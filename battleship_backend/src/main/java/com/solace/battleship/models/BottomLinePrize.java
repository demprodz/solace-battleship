package com.solace.battleship.models;

public class BottomLinePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private boolean isTaken;

    public BottomLinePrize() {
    }

    public BottomLinePrize(int numPrizes) {
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
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

    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int column = 0; column < spots[0].length; column++) {
            if (spots[2][column] != null && spots[2][column].getIsMarked()
                    && numberSet.isNumberMarked(spots[2][column].getValue())) {
                markedSpots++;
            }
        }

        if (markedSpots == 5) {
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
        BottomLinePrize bottomLinePrize = (BottomLinePrize) o;
        return numPrizes == bottomLinePrize.numPrizes && isTaken == bottomLinePrize.isTaken;
    }
}