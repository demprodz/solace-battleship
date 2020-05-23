package com.solace.battleship.models;

public interface IPrize {
    public boolean checkPatternMatch(Ticket ticket, GameNumberSet numberSet);

    public String getPrizeName();

    public String getAbbreviatedName();

    public String getDescription();

    public int getNumPrizes();

    public void setNumPrizes(int numPrizes);

    public boolean getIsTaken();

    public void setIsTaken(boolean isTaken);
}