package com.solace.battleship.models;

public class HousieNumber {
    private int value;
    private boolean isSoftMarked;
    private boolean isHardMarked;

    public HousieNumber() {
        this.isSoftMarked = false;
        this.isHardMarked = false;
    }

    public HousieNumber(int value) {
        this.value = value;
        this.isSoftMarked = false;
        this.isHardMarked = false;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setIsSoftMarked(boolean isSoftMarked) {
        this.isSoftMarked = isSoftMarked;
    }

    public boolean getIsSoftMarked() {
        return isSoftMarked;
    }

    public void setIsHardMarked(boolean isHardMarked) {
        this.isHardMarked = isHardMarked;
    }

    public boolean getIsHardMarked() {
        return isHardMarked;
    }
}