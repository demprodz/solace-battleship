package com.solace.battleship.models;

public class HousieNumber {
    private int value;
    private boolean isMarked;

    public HousieNumber() {
        this.isMarked = false;
    }

    public HousieNumber(int value) {
        this.value = value;
        this.isMarked = false;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public void setIsMarked(boolean isMarked) {
        this.isMarked = isMarked;
    }

    public boolean getIsMarked() {
        return isMarked;
    }
}