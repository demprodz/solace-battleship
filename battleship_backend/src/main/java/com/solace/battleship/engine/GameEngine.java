package com.solace.battleship.engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import com.solace.battleship.events.*;
import com.solace.battleship.models.GameNumberSet;
import com.solace.battleship.models.IPrize;
import com.solace.battleship.models.TicketSet;

import org.springframework.stereotype.Component;

/**
 * GameEngine
 */
@Component
public class GameEngine implements IGameEngine {

  private final Map<String, GameSession> gameSessionMap = new HashMap<>();

  public static final String GAME_ALREADY_STARTED_ERROR = "Game has already started!";
  public static final String SESSION_DOES_NOT_EXIST_ERROR = "Your session has either expired or a server error has occured.";
  public static final String PLAYER_JOIN_SUCCESS = "Player has joined the game successfully!";
  public static final String PLAYER_ALREADY_JOINED_ERROR = "Player has already joined the game!";
  public static final String BOARD_SET_SUCCESS = "Board has been set!";
  public static final String BOARD_SET_SERVER_ERROR = "Something has gone wrong on the server, this is not your fault.";
  public static final String BOARD_ALREADY_SET_ERROR = "Board has already been set!";

  @Override
  public JoinResult requestToJoinGame(PlayerJoined request) {
    String returnMessage = "";
    boolean joinRequestResult;
    TicketSet ticketSet = new TicketSet();

    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new JoinResult();
    }

    if (session.getGameState() != GameState.WAITING_FOR_JOIN) {
      returnMessage = GAME_ALREADY_STARTED_ERROR;
      joinRequestResult = false;
    } else {
      joinRequestResult = session.setPlayerJoined(request);
      // joinRequestResult = session.getGameStart().setPlayerJoined(request);
      if (joinRequestResult) {
        returnMessage = PLAYER_JOIN_SUCCESS;
        ticketSet = session.getPlayerTicketSet(request.getPlayerId());
      } else {
        returnMessage = PLAYER_ALREADY_JOINED_ERROR;
      }
    }

    // if (session.getGameState() != GameState.WAITING_FOR_JOIN) {
    // returnMessage = GAME_ALREADY_STARTED_ERROR;
    // joinRequestResult = false;
    // } else {
    // joinRequestResult = session.getGameStart().setPlayerJoined(request);
    // if (joinRequestResult) {
    // returnMessage = PLAYER_JOIN_SUCCESS;
    // } else {
    // returnMessage = PLAYER_ALREADY_JOINED_ERROR;
    // }
    // }
    return new JoinResult(request.getPlayerId(), request.getPlayerNickname(), joinRequestResult, returnMessage,
        ticketSet);
  }

  @Override
  public SessionCreateResult requestToCreateSession(SessionCreateRequest request) {
    if (gameSessionMap.containsKey(request.getSessionId())) {
      return new SessionCreateResult();
    }

    GameSession session = new GameSession(request.getSessionId(), request.getName(), request.getPlayerJoinUrl());

    gameSessionMap.put(request.getSessionId(), session);

    return new SessionCreateResult(session.getSessionSummary(), true);
  }

  @Override
  public SessionSearchResult requestToSearchSession(SessionSearchRequest request) {
    if (!gameSessionMap.containsKey(request.getSessionId())) {
      return new SessionSearchResult(request.getSessionId(), "Game could not be found");
    }

    if (gameSessionMap.get(request.getSessionId()).isGameStarted()) {
      return new SessionSearchResult(request.getSessionId(), "Game has already started");
    }

    return new SessionSearchResult(request.getSessionId(), true);
  }

  @Override
  public LobbyPageReloadResult requestToGetLobbyPageReload(LobbyPageRequest request) {
    if (gameSessionMap.size() == 0) {
      return new LobbyPageReloadResult();
    }

    return new LobbyPageReloadResult(getSessionSummaries(), true);
  }

  private HashMap<String, SessionSummary> getSessionSummaries() {
    Iterator<Map.Entry<String, GameSession>> it = gameSessionMap.entrySet().iterator();

    HashMap<String, SessionSummary> sessionSummaries = new HashMap<String, SessionSummary>();
    while (it.hasNext()) {
      Map.Entry<String, GameSession> entry = it.next();
      String sessionId = entry.getKey();
      GameSession gameSession = entry.getValue();

      if (!gameSession.isGameStarted()) {
        sessionSummaries.put(sessionId, gameSession.getSessionSummary());
      }
    }

    return sessionSummaries;
  }

  @Override
  public PlayerPageReloadResult requestToGetPlayerPageReload(PlayerPageRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PlayerPageReloadResult();
    }

    Player player = session.getPlayers().get(request.getPlayerId());
    boolean isGameStarted = session.isGameStarted();
    IPrize[] prizes = session.getPrizes();
    return new PlayerPageReloadResult(session.getName(), player, isGameStarted, prizes, player != null);
  }

  @Override
  public AdminPageReloadResult requestToGetAdminPageReload(AdminPageRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new AdminPageReloadResult();
    }

    boolean isGameInProgress = session.isGameInProgress();
    IPrize[] prizes = session.getPrizes();
    GameNumberSet gameNumberSet = session.getGameNumberSet();
    int timer = session.getTimer();
    int numPrizeModePlayers = session.getNumPrizeModePlayers();
    return new AdminPageReloadResult(isGameInProgress, prizes, gameNumberSet, timer, numPrizeModePlayers,
        gameNumberSet != null);
  }

  @Override
  public AdminLandingPageReloadResult requestToGetAdminLandingPageReload(AdminLandingPageRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new AdminLandingPageReloadResult();
    }

    return new AdminLandingPageReloadResult(session.getSessionSummary(), true);
  }

  @Override
  public boolean canGameStart(String sessionId) {
    GameSession session = gameSessionMap.get(sessionId);

    if (session == null) {
      return false;
    }
    return session.getPlayers().size() > 0;
  }

  @Override
  public GameStart getGameStartAndStartGame(GameStartRequest request) {
    String sessionId = request.getSessionId();
    GameSession session = gameSessionMap.get(sessionId);

    if (canGameStart(sessionId)) {
      return session.getGameStart(request.getIsAutoMode(), request.getSelectedTimer(),
          request.getDisabledPrizesIndexList());
    }

    return new GameStart();
  }

  @Override
  public NextNumberChooseResult getNextNumber(String sessionId) {
    GameSession session = gameSessionMap.get(sessionId);

    if (session == null) {
      return new NextNumberChooseResult(sessionId, false, SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.getNextNumber();
    }
  }

  @Override
  public TileSelectResponseEvent requestToSelectTile(TileSelectRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new TileSelectResponseEvent(request.getSessionId(), request.getPlayerId(), false,
          SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.setSelection(request);
    }
  }

  @Override
  public PrizeSubmitResult requestToSubmitPrize(PrizeSubmitRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PrizeSubmitResult(request.getSessionId(), request.getPlayerId(), false, SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.submitPrizeRequest(request);
    }
  }

  @Override
  public PrizeSubmitResult requestToOverridePrize(PrizeSubmitRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PrizeSubmitResult(request.getSessionId(), request.getPlayerId(), false, SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.submitOverridePrizeRequest(request);
    }
  }

  @Override
  public PrizeSubmitResult requestToConfirmDeniedPrize(PrizeSubmitRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PrizeSubmitResult(request.getSessionId(), request.getPlayerId(), false, SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.confirmDeniedPrizeRequest(request);
    }
  }

  @Override
  public boolean updatePrizeStatus(PrizeSubmitRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return false;
    } else {
      return session.updatePrizeStatus(request);
    }
  }

  @Override
  public PrizeStatusResult getPrizeStatus(String sessionId) {
    GameSession session = gameSessionMap.get(sessionId);

    if (session == null) {
      return null;
    } else {
      return session.getPrizeStatus();
    }
  }

  @Override
  public NextNumberConfirmResult requestToConfirmNextNumber(NextNumberConfirmRequest nextNumberConfirmRequest) {
    GameSession session = gameSessionMap.get(nextNumberConfirmRequest.getSessionId());

    if (session == null) {
      return new NextNumberConfirmResult(nextNumberConfirmRequest.getSessionId(), false, SESSION_DOES_NOT_EXIST_ERROR);
    } else {
      return session.confirmNumber(nextNumberConfirmRequest.getRowIndex(), nextNumberConfirmRequest.getColumnIndex());
    }
  }

  @Override
  public PrizeModeResult requestToTurnPrizeModeOn(PrizeModeRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PrizeModeResult();
    } else {
      session.setPrizeModePlayer(request.getPlayerId());
      return new PrizeModeResult(true, session.getNumPrizeModePlayers());
    }
  }

  @Override
  public PrizeModeResult requestToTurnPrizeModeOff(PrizeModeRequest request) {
    GameSession session = gameSessionMap.get(request.getSessionId());

    if (session == null) {
      return new PrizeModeResult();
    } else {
      session.removePrizeModePlayer(request.getPlayerId());
      return new PrizeModeResult(true, session.getNumPrizeModePlayers());
    }
  }
}