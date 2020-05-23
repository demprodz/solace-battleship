package com.solace.battleship.events;

import java.util.Arrays;
import java.util.Objects;

/**
 * An object represening the response to a player's moves
 * 
 * @author Andrew Roberts, Thomas Kunnumpurath
 */
public class TileSelectResponseEvent {

  private String sessionId;
  private String playerId;
  private int ticket;
  private int row;
  private int column;
  private boolean newIsMarked;
  private boolean success;
  private String returnMessage;

  public TileSelectResponseEvent() {
  }

  public TileSelectResponseEvent(String sessionId, String playerId, boolean success, String returnMessage) {
    this.sessionId = sessionId;
    this.playerId = playerId;
    this.success = success;
    this.returnMessage = returnMessage;
  }

  public TileSelectResponseEvent(String sessionId, String playerId, int ticket, int row, int column) {
    this.sessionId = sessionId;
    this.playerId = playerId;
    this.ticket = ticket;
    this.row = row;
    this.column = column;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }

  public String getPlayerId() {
    return playerId;
  }

  public void setPlayerId(String playerId) {
    this.playerId = playerId;
  }

  public int getTicket() {
    return ticket;
  }

  public void setTicket(int ticket) {
    this.ticket = ticket;
  }

  public int getRow() {
    return row;
  }

  public void setRow(int row) {
    this.row = row;
  }

  public int getColumn() {
    return column;
  }

  public void setColumn(int column) {
    this.column = column;
  }

  public boolean getNewIsMarked() {
    return newIsMarked;
  }

  public void setNewIsMarked(boolean isMarked) {
    this.newIsMarked = isMarked;
  }

  public boolean getSuccess() {
    return success;
  }

  public void setSuccess(boolean success) {
    this.success = success;
  }

  public String getReturnMessage() {
    return returnMessage;
  }

  public void setReturnMessage(String returnMessage) {
    this.returnMessage = returnMessage;
  }
}
