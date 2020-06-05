package com.solace.battleship.events;

import java.util.Objects;

import com.solace.battleship.engine.GameState;
import com.solace.battleship.models.IPrize;
import com.solace.battleship.models.TicketSet;

/**
 * An object representing a player and its associated states
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PlayerPageReloadResult {
    private String sessionName;
    private Player player;
    private boolean isGameStarted;
    private IPrize[] prizes;
    private boolean success;

    public PlayerPageReloadResult() {
        this.success = false;
    }

    public PlayerPageReloadResult(String sessionName, Player player, boolean isGameStarted, IPrize[] prizes,
            boolean success) {
        this.sessionName = sessionName;
        this.player = player;
        this.isGameStarted = isGameStarted;
        this.prizes = prizes;
        this.success = success;
    }

    public String getSessionName() {
        return this.sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public PlayerPageReloadResult sessionName(String sessionName) {
        this.sessionName = sessionName;
        return this;
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

    public PlayerPageReloadResult success(boolean success) {
        this.success = success;
        return this;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public boolean isIsGameStarted() {
        return this.isGameStarted;
    }

    public boolean getIsGameStarted() {
        return this.isGameStarted;
    }

    public void setIsGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
    }

    public IPrize[] getPrizes() {
        return this.prizes;
    }

    public void setPrizes(IPrize[] prizes) {
        this.prizes = prizes;
    }

    public PlayerPageReloadResult player(Player player) {
        this.player = player;
        return this;
    }

    public PlayerPageReloadResult isGameStarted(boolean isGameStarted) {
        this.isGameStarted = isGameStarted;
        return this;
    }

    public PlayerPageReloadResult prizes(IPrize[] prizes) {
        this.prizes = prizes;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PlayerPageReloadResult)) {
            return false;
        }
        PlayerPageReloadResult playerPageReloadResult = (PlayerPageReloadResult) o;
        return Objects.equals(player, playerPageReloadResult.player)
                && isGameStarted == playerPageReloadResult.isGameStarted
                && Objects.equals(prizes, playerPageReloadResult.prizes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(player, isGameStarted, prizes);
    }

    @Override
    public String toString() {
        return "{" + " player='" + getPlayer() + "'" + ", isGameStarted='" + isIsGameStarted() + "'" + ", prizes='"
                + getPrizes() + "'" + "}";
    }

}
