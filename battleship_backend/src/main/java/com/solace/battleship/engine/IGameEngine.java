package com.solace.battleship.engine;

import com.solace.battleship.events.*;
import com.solace.battleship.models.IPrize;

/**
 * Interface for the battleship game engine. Determines all rules and functions
 * associated with the game.
 * 
 * @Author Andrew Roberts, Thomas Kunnumpurath
 */
public interface IGameEngine {

  /**
   * Function to request for a game to be joined
   * 
   * @param request A player join request
   * @return The result of a Join request
   */
  public JoinResult requestToJoinGame(PlayerJoined request);

  public SessionCreateResult requestToCreateSession(SessionCreateRequest request);

  public SessionSearchResult requestToSearchSession(SessionSearchRequest request);

  public LobbyPageReloadResult requestToGetLobbyPageReload(LobbyPageRequest request);

  public PlayerPageReloadResult requestToGetPlayerPageReload(PlayerPageRequest request);

  public AdminPageReloadResult requestToGetAdminPageReload(AdminPageRequest request);

  public AdminLandingPageReloadResult requestToGetAdminLandingPageReload(AdminLandingPageRequest request);

  public NextNumberConfirmResult requestToConfirmNextNumber(NextNumberConfirmRequest nextNumberConfirmRequest);

  /**
   * Function to check if a game can start
   *
   * @param sessionId
   * @return true if both players have joined
   */
  public boolean canGameStart(String sessionId);

  /**
   * Function to get the GameStart event
   *
   * @param request
   * @return a GameStart event object
   */
  public GameStart getGameStartAndStartGame(GameStartRequest request);

  /**
   * Function to get next random number from the master table
   * 
   * @param sessionId
   * @return a NextNumberChooseResult object
   */
  public NextNumberChooseResult getNextNumber(String sessionId);

  /**
   * Function to request selecting tile on player's ticket
   * 
   * @param request
   * @return The result of the select tile request
   */
  public TileSelectResponseEvent requestToSelectTile(TileSelectRequest request);

  /**
   * Function to request submitting ticket for prize
   * 
   * @param request
   * @return The result of the submit prize request
   */
  public PrizeSubmitResult requestToSubmitPrize(PrizeSubmitRequest request);

  public PrizeSubmitResult requestToOverridePrize(PrizeSubmitRequest request);

  public PrizeSubmitResult requestToConfirmDeniedPrize(PrizeSubmitRequest request);

  public boolean updatePrizeStatus(PrizeSubmitRequest request);

  public PrizeStatusResult getPrizeStatus(String sessionId);

  public PrizeModeResult requestToTurnPrizeModeOn(PrizeModeRequest request);

  public PrizeModeResult requestToTurnPrizeModeOff(PrizeModeRequest request);
}
