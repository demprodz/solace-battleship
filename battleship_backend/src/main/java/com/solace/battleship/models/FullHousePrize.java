package com.solace.battleship.models;

public class FullHousePrize implements IPrize {
    private int numPrizes;
    private boolean isTaken;

    public FullHousePrize() {
    }

    public FullHousePrize(int numPrizes) {
        this.numPrizes = numPrizes;
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

    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet) {
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
            numPrizes--;

            if (numPrizes == 0) {
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
        FullHousePrize fullHousePrize = (FullHousePrize) o;
        return numPrizes == fullHousePrize.numPrizes && isTaken == fullHousePrize.isTaken;
    }
}