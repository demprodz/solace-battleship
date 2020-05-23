package com.solace.battleship.models;

public class TopLinePrize implements IPrize {
    private int numPrizes;
    private boolean isTaken;

    public TopLinePrize() {
    }

    public TopLinePrize(int numPrizes) {
        this.numPrizes = numPrizes;
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

    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet) {
        Spot[][] spots = ticket.getTicketMatrix();

        int markedSpots = 0;
        for (int column = 0; column < spots[0].length; column++) {
            if (spots[0][column] != null && spots[0][column].getIsMarked()
                    && numberSet.isNumberMarked(spots[0][column].getValue())) {
                markedSpots++;
            }
        }

        if (markedSpots == 5) {
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
        TopLinePrize topLinePrize = (TopLinePrize) o;
        return numPrizes == topLinePrize.numPrizes && isTaken == topLinePrize.isTaken;
    }
}