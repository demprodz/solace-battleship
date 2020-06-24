package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a PrizeModeOnResult request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class PrizeModeResult {
    private boolean success;
    private int numPrizeModePlayers;

    public PrizeModeResult() {
        this.success = false;
    }

    public PrizeModeResult(boolean success, int numPrizeModePlayers) {
        this.success = success;
        this.numPrizeModePlayers = numPrizeModePlayers;
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

    public int getNumPrizeModePlayers() {
        return this.numPrizeModePlayers;
    }

    public void setNumPrizeModePlayers(int numPrizeModePlayers) {
        this.numPrizeModePlayers = numPrizeModePlayers;
    }

    public PrizeModeResult success(boolean success) {
        this.success = success;
        return this;
    }

    public PrizeModeResult numPrizeModePlayers(int numPrizeModePlayers) {
        this.numPrizeModePlayers = numPrizeModePlayers;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof PrizeModeResult)) {
            return false;
        }
        PrizeModeResult prizeModeResult = (PrizeModeResult) o;
        return success == prizeModeResult.success && numPrizeModePlayers == prizeModeResult.numPrizeModePlayers;
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, numPrizeModePlayers);
    }

    @Override
    public String toString() {
        return "{" + " success='" + isSuccess() + "'" + ", numPrizeModePlayers='" + getNumPrizeModePlayers() + "'"
                + "}";
    }

}
