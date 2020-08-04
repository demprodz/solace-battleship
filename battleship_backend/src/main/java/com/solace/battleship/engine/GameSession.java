package com.solace.battleship.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
  private int timer;
  private HashSet<String> prizeModePlayers;

  public GameSession(String sessionId, String name, String playerJoinUrl) {
    this.sessionId = sessionId;
    this.name = name;
    this.playerJoinUrl = playerJoinUrl;
    this.gameState = GameState.WAITING_FOR_JOIN;
    this.gameStart = new GameStart(sessionId);
    this.players = new HashMap<String, Player>();
    this.numberSet = new GameNumberSet();
    this.timer = 10;
    this.prizeModePlayers = new HashSet<String>();
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

  public int getTimer() {
    return this.timer;
  }

  public void setTimer(int timer) {
    this.timer = timer;
  }

  public void setPrizeModePlayer(String playerId) {
    if (!players.containsKey(playerId)) {
      return;
    }

    prizeModePlayers.add(playerId);
  }

  public void removePrizeModePlayer(String playerId) {
    prizeModePlayers.remove(playerId);
  }

  public int getNumPrizeModePlayers() {
    return prizeModePlayers.size();
  }

  public GameStart getGameStart(boolean isAutoMode, String selectedTimer, int[] disabledPrizesIndexList) {
    numberSet.setPrizes(getEnabledPrizeList(disabledPrizesIndexList));

    gameStart.setPlayers(players);
    gameStart.setGameNumberSet(numberSet);
    gameStart.setIsAutoMode(isAutoMode);

    switch (selectedTimer) {
      case "10 seconds":
        this.timer = 10;
        break;
      case "30 seconds":
        this.timer = 30;
        break;
      case "45 seconds":
        this.timer = 45;
        break;
      case "60 seconds":
        this.timer = 60;
        break;
      default:
        this.timer = 10;
    }

    gameStart.setTimer(this.timer);

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

  public NextNumberConfirmResult confirmNumber(int rowIndex, int columnIndex) {
    boolean isGameOver = this.numberSet.confirmNumberAndCheckGameOver(rowIndex, columnIndex);

    return new NextNumberConfirmResult(sessionId, true, isGameOver);
  }

  public PrizeSubmitResult submitPrizeRequest(PrizeSubmitRequest request) {
    Player player = players.get(request.getPlayerId());
    PrizeCheckerResponse response = PrizeChecker.validatePrizeRequest(request, player, this.numberSet, false);

    return new PrizeSubmitResult(request.getSessionId(), player.getId(), player.getName(), request.getTicket(),
        request.getSelectedPrizeIndex(), response.equals(PrizeCheckerResponse.SUCCESS), response.message, response);
  }

  public PrizeSubmitResult submitOverridePrizeRequest(PrizeSubmitRequest request) {
    Player player = players.get(request.getPlayerId());
    PrizeCheckerResponse response = PrizeChecker.validatePrizeRequest(request, player, this.numberSet, true);

    return new PrizeSubmitResult(request.getSessionId(), player.getId(), player.getName(), request.getTicket(),
        request.getSelectedPrizeIndex(), response.equals(PrizeCheckerResponse.SUCCESS), response.message, response);
  }

  public PrizeSubmitResult confirmDeniedPrizeRequest(PrizeSubmitRequest request) {
    Player player = players.get(request.getPlayerId());

    this.players.get(request.getPlayerId()).getTicketSet().getTicket(request.getTicket()).setIsEliminated(true);

    return new PrizeSubmitResult(request.getSessionId(), player.getId(), player.getName(), request.getTicket(),
        request.getSelectedPrizeIndex(), false, PrizeCheckerResponse.FAILURE.message, PrizeCheckerResponse.FAILURE);
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

  // Remove disabled prizes from final prize list
  private IPrize[] getEnabledPrizeList(int[] disabledPrizesIndexList) {
    for (int i : disabledPrizesIndexList) {
      this.numberSet.getPrizes()[i].setIsEnabled(false);
    }

    ArrayList<IPrize> enabledPrizeList = new ArrayList<IPrize>();
    for (IPrize prize : this.numberSet.getPrizes()) {
      if (prize.getIsEnabled()) {
        enabledPrizeList.add(prize);
      }
    }

    IPrize[] finalEnabledPrizeList = new IPrize[this.numberSet.getPrizes().length - disabledPrizesIndexList.length];

    return enabledPrizeList.toArray(finalEnabledPrizeList);
  }
}