package com.solace.battleship.events;

/**
 * An object representing a NextNumberChooseRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class NextNumberChooseResult {
    private String sessionId;
    private int value;
    private int rowIndex;
    private int columnIndex;
    private boolean success;
    private boolean isGameOver;
    private String returnMessage;

    public String getSessionId() {
        return sessionId;
    }

    public NextNumberChooseResult() {
        success = false;
        isGameOver = true;
    }

    public NextNumberChooseResult(int value, int rowIndex, int columnIndex, boolean isGameOver) {
        this.value = value;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
        this.isGameOver = isGameOver;
    }

    public NextNumberChooseResult(String sessionId, boolean success, String returnMessage) {
        this.success = success;
        this.returnMessage = returnMessage;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getRowIndex() {
        return this.rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public int getColumnIndex() {
        return this.columnIndex;
    }

    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
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

    public String getReturnMessage() {
        return this.returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    public boolean getIsGameOver() {
        return this.isGameOver;
    }

    public void setIsGameOver(boolean isGameOver) {
        this.isGameOver = isGameOver;
    }
}
