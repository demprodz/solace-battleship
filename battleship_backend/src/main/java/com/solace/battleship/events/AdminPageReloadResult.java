package com.solace.battleship.events;

import java.util.Objects;

import com.solace.battleship.models.GameNumberSet;
import com.solace.battleship.models.IPrize;

/**
 * An object representing a player and its associated states
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class AdminPageReloadResult {
    private boolean isGameInProgress;
    private IPrize[] prizes;
    private GameNumberSet gameNumberSet;
    private int timer;
    private boolean success;

    public AdminPageReloadResult() {
        this.success = false;
    }

    public AdminPageReloadResult(boolean isGameInProgress, IPrize[] prizes, GameNumberSet gameNumberSet, int timer,
            boolean success) {
        this.isGameInProgress = isGameInProgress;
        this.prizes = prizes;
        this.gameNumberSet = gameNumberSet;
        this.timer = timer;
        this.success = success;
    }

    public boolean isIsGameInProgress() {
        return this.isGameInProgress;
    }

    public boolean getIsGameInProgress() {
        return this.isGameInProgress;
    }

    public void setIsGameInProgress(boolean isGameInProgress) {
        this.isGameInProgress = isGameInProgress;
    }

    public IPrize[] getPrizes() {
        return this.prizes;
    }

    public void setPrizes(IPrize[] prizes) {
        this.prizes = prizes;
    }

    public GameNumberSet getGameNumberSet() {
        return this.gameNumberSet;
    }

    public void setGameNumberSet(GameNumberSet gameNumberSet) {
        this.gameNumberSet = gameNumberSet;
    }

    public int getTimer() {
        return this.timer;
    }

    public void setTimer(int timer) {
        this.timer = timer;
    }

    public boolean isSuccess() {
        return this.success;
    }

    public boolean getSuccess() {
        return this.success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public AdminPageReloadResult isGameInProgress(boolean isGameInProgress) {
        this.isGameInProgress = isGameInProgress;
        return this;
    }

    public AdminPageReloadResult prizes(IPrize[] prizes) {
        this.prizes = prizes;
        return this;
    }

    public AdminPageReloadResult gameNumberSet(GameNumberSet gameNumberSet) {
        this.gameNumberSet = gameNumberSet;
        return this;
    }

    public AdminPageReloadResult success(boolean success) {
        this.success = success;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof AdminPageReloadResult)) {
            return false;
        }
        AdminPageReloadResult adminPageReloadResult = (AdminPageReloadResult) o;
        return isGameInProgress == adminPageReloadResult.isGameInProgress
                && Objects.equals(prizes, adminPageReloadResult.prizes)
                && Objects.equals(gameNumberSet, adminPageReloadResult.gameNumberSet)
                && success == adminPageReloadResult.success;
    }

    @Override
    public int hashCode() {
        return Objects.hash(isGameInProgress, prizes, gameNumberSet, success);
    }

    @Override
    public String toString() {
        return "{" + " isGameInProgress='" + isIsGameInProgress() + "'" + ", prizes='" + getPrizes() + "'"
                + ", gameNumberSet='" + getGameNumberSet() + "'" + ", success='" + isSuccess() + "'" + "}";
    }
}
