package com.solace.battleship.models;

public class Spot {
    int value;
    boolean isMarked;

    public Spot() {
    }

    public Spot(int value) {
        this.value = value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public boolean toggleMarked() {
        this.isMarked = !this.isMarked;
        return this.isMarked;
    }

    public boolean getIsMarked() {
        return isMarked;
    }
}