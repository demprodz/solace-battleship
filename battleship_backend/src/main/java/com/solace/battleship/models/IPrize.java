package com.solace.battleship.models;

import java.util.ArrayList;

import com.solace.battleship.events.Player;

public interface IPrize {
    public boolean checkPatternMatch(Player player, Ticket ticket, GameNumberSet numberSet);

    public String getPrizeName();

    public String getAbbreviatedName();

    public String getDescription();

    public int getNumPrizes();

    public void setNumPrizes(int numPrizes);

    public int getNumClaimedPrizes();

    public boolean getIsEnabled();

    public void setIsEnabled(boolean isEnabled);

    public boolean getIsTaken();

    public void setIsTaken(boolean isTaken);

    public ArrayList<String> getWinners();

    public void setWinners(ArrayList<String> winners);
}