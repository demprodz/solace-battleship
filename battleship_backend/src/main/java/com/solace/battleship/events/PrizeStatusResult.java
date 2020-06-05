package com.solace.battleship.events;

import java.util.Objects;

import com.solace.battleship.models.IPrize;

/**
 * An object representing a GameStartRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PrizeStatusResult {
    private IPrize[] prizes;
    private boolean isGameOver;

    public PrizeStatusResult() {
    }

    public PrizeStatusResult(IPrize[] prizes, boolean isGameOver) {
        this.prizes = prizes;
        this.isGameOver = isGameOver;
    }

    public IPrize[] getPrizes() {
        return this.prizes;
    }

    public void setPrizes(IPrize[] prizes) {
        this.prizes = prizes;
    }

    public boolean isIsGameOver() {
        return this.isGameOver;
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }

    public PrizeStatusResult prizes(IPrize[] prizes) {
        this.prizes = prizes;
        return this;
    }

    public PrizeStatusResult isGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PrizeStatusResult)) {
            return false;
        }
        PrizeStatusResult prizeStatusResult = (PrizeStatusResult) o;
        return Objects.equals(prizes, prizeStatusResult.prizes) && isGameOver == prizeStatusResult.isGameOver;
    }

    @Override
    public int hashCode() {
        return Objects.hash(prizes, isGameOver);
    }

    @Override
    public String toString() {
        return "{" + " prizes='" + getPrizes() + "'" + ", isGameOver='" + isIsGameOver() + "'" + "}";
    }

}
