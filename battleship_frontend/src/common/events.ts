import { AdminLandingPage } from "./../controller-app/admin-landing-page";
/**
 * This shared library represents all the events and event related objects that are available to the battleship application
 */

export class TopicHelper {
  private _prefix: string;

  get prefix(): string {
    return this._prefix;
  }

  set prefix(prefix: string) {
    this._prefix = prefix;
  }
}

export type PlayerName = "player1" | "player2";

export class Session {
  name: string;
  joinUrl: string;
}

export class SessionCreateRequest {
  name: string;
  sessionId: string;
  playerJoinUrl: string;
}

export class SessionCreateResult {
  success: boolean;
  sessionSummary: SessionSummary;
}

export class SearchSessionRequest {
  sessionId: string;
}

export class SearchSessionResult {
  sessionId: string;
  success: boolean;
  errorMessage: string;
}

export class LobbyPageRequest {}

export class LobbyPageReloadResult {
  success: boolean;
  sessions: Map<String, SessionSummary>;
}

export class SessionSummary {
  sessionId: string;
  name: string;
  playerJoinUrl: string;
  players: { [key: string]: Player };
  numPlayers: number;
  prizes: IPrize[];
  isGameInProgress: boolean;
}

/**
 * Object that represents the player in a game
 * name: name of the player
 * nickname: nickname for the player
 * boardState: state of the player's board
 * publicBoardState: public state of the board
 * isTurn: whether its the players turn
 * @author: Thomas Kunnumpurath, Andrew Roberts
 */
export class Player {
  id: string;
  name: string;
  sessionId: string;
  ticketSet: TicketSet;
  numTickets: number;
}

export class PlayerPageRequest {
  sessionId: string;
  playerId: string;
}

export class PlayerPageReloadResult {
  sessionName: string;
  player: Player;
  isGameStarted: boolean;
  prizes: IPrize[];
  success: boolean;
}

export class AdminDashboardPageRequest {
  sessionId: string;
}

export class AdminDashboardPageReloadResult {
  isGameInProgress: boolean;
  gameNumberSet: GameNumberSet;
  prizes: IPrize[];
  success: boolean;
}

export class AdminLandingPageRequest {
  sessionId: string;
}

export class AdminLandingPageReloadResult {
  sessionSummary: SessionSummary;
  success: boolean;
}

/**
 * Object that represents the player joined event
 * playerName: the name of the player (Player1 or player2)
 * playerNickname: the nickname of hte player
 */
export class PlayerJoined {
  playerId: string;
  playerName: PlayerName;
  playerNickname: string;
  sessionId: string;
  numTickets: number;
}

/**
 * Object that represents the start event of the game
 */
export class GameStart {
  sessionId: string;
  players: { [key: string]: Player };
  gameNumberSet: GameNumberSet;
  success: boolean;
}

export class GameStartRequest {
  sessionId: string;
}

export class GameNumberSet {
  numberSet: HousieNumber[][];
  prizes: IPrize[];
  numbersLeft: number;
}

export class HousieNumber {
  value: number;
  isMarked: boolean;
}

export class IPrize {
  prizeName: string;
  abbreviatedName: string;
  description: string;
  numPrizes: number;
  numClaimedPrizes: number;
  isTaken: boolean;
  winners: string[];
}

export class PrizeStatusResult {
  prizes: IPrize[];
  isGameOver: boolean;
}

export class NextNumberChooseEvent {
  sessionId: string;
}

export class NextNumberChooseResult {
  sessionId: string;
  value: number;
  rowIndex: number;
  columnIndex: number;
  success: boolean;
  isGameOver: boolean;
  returnMessage: string;
}

export class PrizeSubmitEvent {
  sessionId: string;
  playerId: string;
  ticket: number;
  selectedPrizeIndex: number;
}

export class PrizeSubmitResult {
  sessionId: string;
  playerId: string;
  ticket: number;
  selectedPrizeIndex: number;
  success: boolean;
  returnMessage: string;
  responseType: string;
}

// CellState for the Board
export type PrivateBoardCellState = "ship" | "empty";

// Cell state for the opponent's board
export type KnownBoardCellState = "hit" | "miss" | "empty";

export class MatchEnd {
  player1Score: number;
  player2Score: number;
  sessionId: String;
}
/**
 * Object that represents a players move response
 * Author: Thomas Kunnumpurath, Andrew Roberts
 */
export class Move {
  player: PlayerName;
  x: number;
  y: number;
  sessionId: string;
}

/**
 * Object representing the response to a move
 * player - The name of the player
 * playerBoard - Represents the public state of Player2's board
 * move - represents the move that result of the move that was just made
 * @author: Thomas Kunnumpurath, Andrew Roberts
 */
export class MoveResponseEvent {
  player: PlayerName;
  playerBoard: KnownBoardCellState[][];
  move: Move;
  moveResult: PrivateBoardCellState;
  sessionId: string;
}

/**
 * Object representing a boardset event
 */

export class BoardSetEvent {
  playerName: PlayerName;
  board: PrivateBoardCellState[][];
  sessionId: string;
}

export class TileSelectEvent {
  sessionId: string;
  playerId: String;
  ticket: number;
  row: number;
  column;
}

export class TileSelectResult {
  sessionId: string;
  playerId: string;
  ticket: number;
  row: number;
  column: number;
  newIsMarked: boolean;
  success: boolean;
  returnMessage: string;
}

/**
 * Object representing a join result
 */

export class JoinResult {
  playerId: string;
  playerNickname: string;
  success: boolean;
  message: string;
  sessionId: string;
  ticketSet: TicketSet;
}

export class TicketSet {
  tickets: Ticket[];
}

export class Ticket {
  ticketNumber: number;
  ticketMatrix: Spot[][];
  populatedSpots: number;
  markedSpots: number;
  isEliminated: boolean;
  foundEarlyFive: boolean;
  foundTopLine: boolean;
  foundMiddleLine: boolean;
  foundBottomLine: boolean;
  foundFullHouse: boolean;
}

export class Spot {
  value: number;
  isMarked: boolean;
}

/**
 * Object representing a Board-Set Result
 */
export class BoardSetResult {
  playerName: PlayerName;
  success: boolean;
  message: string;
  sessionId: string;
}

/**
 * Object representing a MATCH-START
 */
export class MatchStart {
  player1Board: BoardSetResult;
  player2Board: BoardSetResult;
  sessionId: string;
}

/**
 * Internal event for moves
 */
export class InternalMoveResult {
  player: PlayerName;
  score: number;
  action: PrivateBoardCellState;
}
