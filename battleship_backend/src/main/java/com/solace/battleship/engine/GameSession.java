package com.solace.battleship.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.solace.battleship.events.*;
import com.solace.battleship.helpers.TicketGenerator;
import com.solace.battleship.helpers.PrizeChecker;
import com.solace.battleship.models.TicketSet;
import com.solace.battleship.models.GameNumberSet;
import com.solace.battleship.models.IPrize;
import com.solace.battleship.models.PrizeCheckerResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object represents a game's session
 */
public class GameSession {

  private static final Logger log = LoggerFactory.getLogger(GameSession.class);

  private GameState gameState;
  private final String sessionId;
  private final String name;
  private final String playerJoinUrl;

  private GameStart gameStart;
  private HashMap<String, Player> players;
  private GameNumberSet numberSet;

  public GameSession(String sessionId, String name, String playerJoinUrl) {
    this.sessionId = sessionId;
    this.name = name;
    this.playerJoinUrl = playerJoinUrl;
    this.gameState = GameState.WAITING_FOR_JOIN;
    this.gameStart = new GameStart(sessionId);
    this.players = new HashMap<String, Player>();
    this.numberSet = new GameNumberSet();
  }

  public String getPlayerJoinUrl() {
    return this.playerJoinUrl;
  }

  public String getSessionId() {
    return this.sessionId;
  }

  public GameState getGameState() {
    return this.gameState;
  }

  public void setGameState(GameState gameState) {
    this.gameState = gameState;
  }

  public GameStart getGameStart() {
    gameStart.setPlayers(players);
    gameStart.setGameNumberSet(numberSet);

    if (players.size() > 0) {
      gameStart.setSuccess(true);
    }

    return gameStart;
  }

  public void setGameStart(GameStart gameStart) {
    this.gameStart = gameStart;
  }

  public boolean isGameStarted() {
    return this.gameStart.getSuccess();
  }

  public GameNumberSet getGameNumberSet() {
    return this.numberSet;
  }

  public NextNumberChooseResult getNextNumber() {
    if (this.numberSet.getNumbersLeft() == 0) {
      return new NextNumberChooseResult();
    }

    NextNumberChooseResult nextNumberChooseResult = this.numberSet.chooseNextNumber();
    nextNumberChooseResult.setSuccess(true);
    nextNumberChooseResult.setSessionId(sessionId);

    return nextNumberChooseResult;
  }

  public PrizeSubmitResult submitPrizeRequest(PrizeSubmitRequest request) {
    PrizeCheckerResponse response = PrizeChecker.validatePrizeRequest(request, players.get(request.getPlayerId()),
        this.numberSet);

    if (response.equals(PrizeCheckerResponse.FAILURE)) {
      this.players.get(request.getPlayerId()).getTicketSet().getTicket(request.getTicket()).setIsEliminated(true);
    }

    return new PrizeSubmitResult(request.getSessionId(), request.getPlayerId(), request.getTicket(),
        request.getSelectedPrizeIndex(), response.equals(PrizeCheckerResponse.SUCCESS), response.message, response);
  }

  public boolean updatePrizeStatus(PrizeSubmitRequest request) {
    return this.numberSet.getPrizes()[request.getSelectedPrizeIndex()].getIsTaken();
  }

  public IPrize[] getPrizes() {
    return this.numberSet.getPrizes();
  }

  public PrizeStatusResult getPrizeStatus() {
    return new PrizeStatusResult(this.numberSet.getPrizes(), this.numberSet.isAllPrizesTaken());
  }

  public boolean isGameInProgress() {
    return isGameStarted() && !this.numberSet.isAllPrizesTaken() && this.numberSet.getNumbersLeft() > 0;
  }

  public boolean setPlayerJoined(PlayerJoined joinRequest) {
    if (players.containsKey(joinRequest.getPlayerId())) {
      return false;
    }

    TicketSet playerTickets = TicketGenerator.createTicketSet(joinRequest.getNumTickets());

    Player player = new Player(joinRequest.getSessionId(), joinRequest.getPlayerId(), joinRequest.getPlayerNickname(),
        playerTickets, joinRequest.getNumTickets());

    players.put(joinRequest.getPlayerId(), player);

    return true;
  }

  public TileSelectResponseEvent setSelection(TileSelectRequest request) {
    TileSelectResponseEvent response = new TileSelectResponseEvent(request.getSessionId(), request.getPlayerId(),
        request.getTicket(), request.getRow(), request.getColumn());

    response.setNewIsMarked(players.get(request.getPlayerId()).getTicketSet().getTicket(request.getTicket())
        .markSpot(request.getRow(), request.getColumn()));
    response.setSuccess(true);

    return response;
  }

  public TicketSet getPlayerTicketSet(String playerId) {
    return players.containsKey(playerId) ? players.get(playerId).getTicketSet() : null;
  }

  public HashMap<String, Player> getPlayers() {
    return players;
  }

  public String getName() {
    return this.name;
  }

  public SessionSummary getSessionSummary() {
    return new SessionSummary(this.sessionId, this.name, this.playerJoinUrl, this.players, this.getPrizes(),
        this.isGameInProgress());
  }
}