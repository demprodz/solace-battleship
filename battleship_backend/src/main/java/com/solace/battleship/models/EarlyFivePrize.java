package com.solace.battleship.models;

public class EarlyFivePrize implements IPrize {
    private int numPrizes;
    private boolean isTaken;

    public EarlyFivePrize() {
    }

    public EarlyFivePrize(int numPrizes) {
        this.numPrizes = numPrizes;
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

        if (markedSpots >= 5) {
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
        EarlyFivePrize earlyFivePrize = (EarlyFivePrize) o;
        return numPrizes == earlyFivePrize.numPrizes && isTaken == earlyFivePrize.isTaken;
    }
}