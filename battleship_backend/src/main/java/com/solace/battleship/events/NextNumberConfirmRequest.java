package com.solace.battleship.events;

import java.util.Objects;

/**
 * An object representing a NextNumberConfirmRequest request
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class NextNumberConfirmRequest {
    private String sessionId;
    private int rowIndex;
    private int columnIndex;

    public NextNumberConfirmRequest() {
    }

    public NextNumberConfirmRequest(String sessionId, int rowIndex, int columnIndex) {
        this.sessionId = sessionId;
        this.rowIndex = rowIndex;
        this.columnIndex = columnIndex;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
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

    public NextNumberConfirmRequest sessionId(String sessionId) {
        this.sessionId = sessionId;
        return this;
    }

    public NextNumberConfirmRequest rowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
        return this;
    }

    public NextNumberConfirmRequest columnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this)
            return true;
        if (!(o instanceof NextNumberConfirmRequest)) {
            return false;
        }
        NextNumberConfirmRequest nextNumberConfirmRequest = (NextNumberConfirmRequest) o;
        return Objects.equals(sessionId, nextNumberConfirmRequest.sessionId)
                && rowIndex == nextNumberConfirmRequest.rowIndex && columnIndex == nextNumberConfirmRequest.columnIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(sessionId, rowIndex, columnIndex);
    }

    @Override
    public String toString() {
        return "{" + " sessionId='" + getSessionId() + "'" + ", rowIndex='" + getRowIndex() + "'" + ", columnIndex='"
                + getColumnIndex() + "'" + "}";
    }

}
