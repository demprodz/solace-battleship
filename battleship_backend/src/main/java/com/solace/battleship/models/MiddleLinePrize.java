package com.solace.battleship.models;

public class MiddleLinePrize implements IPrize {
    private int numPrizes;
    private int numClaimedPrizes;
    private boolean isTaken;

    public MiddleLinePrize() {
    }

    public MiddleLinePrize(int numPrizes) {
        this.numPrizes = numPrizes;
        this.numClaimedPrizes = 0;
    }

    public String getPrizeName() {
        return "Middle Line";
    }

    public String getAbbreviatedName() {
        return "ML";
    }

    public String getDescription() {
        return "The ticket with all the numbers of the middle row marked.";
    }

    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int column = 0; column < spots[0].length; column++) {
            if (spots[1][column] != null && spots[1][column].getIsMarked()
                    && numberSet.isNumberMarked(spots[1][column].getValue())) {
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
        MiddleLinePrize middleLinePrize = (MiddleLinePrize) o;
        return numPrizes == middleLinePrize.numPrizes && isTaken == middleLinePrize.isTaken;
    }
}