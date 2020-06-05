import {
  TileSelectEvent,
  TileSelectResult,
  Ticket,
  NextNumberChooseResult,
  PrizeSubmitEvent,
  IPrize,
  PrizeSubmitResult,
  PlayerPageRequest,
  PlayerPageReloadResult,
  PrizeStatusResult,
} from "./../common/events";
import { WAITING_STATE, IN_PROGRESS_STATE, GAME_OVER_STATE } from "./../common/constants";
import { bindable, inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";
import { BoardSetEvent, Player, PlayerName, PrivateBoardCellState, KnownBoardCellState, TopicHelper, BoardSetResult, GameStart } from "../common/events";
import { GameParams } from "common/game-params";
import "./housie-table.css";

/**
 * Responsible for setting the board
 * @author Thomas Kunnumpurath, Andrew Roberts
 */
@inject(Router, SolaceClient, Player, GameParams, TopicHelper)
export class HousieTable {
  //State for the board
  private pageState = WAITING_STATE;
  private currentNumber: number;
  private showPrizes: boolean;

  @bindable
  private prizes: IPrize[];
  private selectedPrizeIndex: number;

  @bindable
  private selectTicketMode: boolean;

  @bindable
  private tickets: Ticket[];

  private countdown: number = 0;
  private error: string = "";
  private playerId: string;
  private sessionName: string;
  private sessionId: string;

  constructor(private router: Router, private solaceClient: SolaceClient, private player: Player, private gameParams: GameParams, private topicHelper: TopicHelper) {}

  activate(params, routeConfig) {
    this.sessionId = params.sessionId;
    this.playerId = params.playerId;

    if (this.solaceClient.session == null) {
      //Connect to Solace
      this.solaceClient.connect().then(() => {
        this.reloadPlayerPageFromServer();
      });
    } else {
      this.reloadPlayerPageFromServer();
      // this.prepareSolaceSubscriptions();
      // this.tickets = this.player.ticketSet.tickets;
    }
  }

  prepareSolaceSubscriptions() {
    //WARM-UP THE TILE-SELECT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/TILE-SELECT-REPLY/${this.player.id}/CONTROLLER`);

    //WARM-UP THE PRIZE-SUBMIT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/PRIZE-SUBMIT-REPLY/${this.player.id}/CONTROLLER`);

    //Subscribe to the GAME-START event
    this.solaceClient.subscribe(
      `${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`,
      // game start event handler callback
      (msg) => {
        let gameStart: GameStart = JSON.parse(msg.getBinaryAttachment());
        if (gameStart.success) {
          this.loading(() => {
            this.pageState = IN_PROGRESS_STATE;

            this.prizes = gameStart.gameNumberSet.prizes;
          });
        }
      }
    );

    //Subscribe to the NEXTNUMBER-CHOOSE-REPLY event
    this.solaceClient.subscribe(
      `${this.topicHelper.prefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`,
      // game start event handler callback
      (msg) => {
        let nextNumberChooseResult: NextNumberChooseResult = JSON.parse(msg.getBinaryAttachment());
        if (nextNumberChooseResult.success) {
          this.currentNumber = nextNumberChooseResult.value;

          if (nextNumberChooseResult.isGameOver) {
            this.pageState = GAME_OVER_STATE;
          }
        } else {
          this.error = nextNumberChooseResult.returnMessage;
        }
      }
    );

    //Subscribe to the UPDATE-PRIZE-STATUS event
    this.solaceClient.subscribe(
      `${this.topicHelper.prefix}/UPDATE-PRIZE-STATUS/CONTROLLER`,
      // game start event handler callback
      (msg) => {
        let prizeStatusResult: PrizeStatusResult = JSON.parse(msg.getBinaryAttachment());

        if (prizeStatusResult != null) {
          this.prizes = prizeStatusResult.prizes;

          if (prizeStatusResult.isGameOver) {
            this.pageState = GAME_OVER_STATE;
          }
        }
      }
    );
  }

  reloadPlayerPageFromServer() {
    let playerPageRequest: PlayerPageRequest = new PlayerPageRequest();
    playerPageRequest.sessionId = this.sessionId;
    playerPageRequest.playerId = this.playerId;

    //WARM-UP THE PLAYER-PAGE-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/PLAYER-PAGE-REPLY/${this.playerId}/CONTROLLER`);

    //Send the request to get player status (upon refresh)
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/PLAYER-PAGE-REQUEST/${this.playerId}`, JSON.stringify(playerPageRequest), `${this.topicHelper.prefix}/PLAYER-PAGE-REPLY/${this.playerId}/CONTROLLER`)
      .then((msg: any) => {
        let playerPageReloadResult: PlayerPageReloadResult = JSON.parse(msg.getBinaryAttachment());
        if (playerPageReloadResult.success) {
          this.player = playerPageReloadResult.player;
          this.tickets = this.player.ticketSet.tickets;
          this.sessionName = playerPageReloadResult.sessionName;

          this.pageState = playerPageReloadResult.isGameStarted ? IN_PROGRESS_STATE : WAITING_STATE;
          this.prizes = playerPageReloadResult.prizes;
        }

        this.prepareSolaceSubscriptions();
      })
      .catch((err) => {
        this.error = err;
      });
  }

  loading(callback) {
    var timeleft = 3;
    var downloadTimer = setInterval(() => {
      if (timeleft <= 0) {
        clearInterval(downloadTimer);
        callback();
      }
      this.countdown += 1;
      timeleft -= 1;
    }, 1000);
  }

  /**
   * Set the state of the player's board
   * @param ticket the individual ticket
   * @param row the row for the board piece
   * @param column the column for the board piece
   */
  tileSelectEvent(ticket: number, row: number, column: number) {
    let tileSelectEvent: TileSelectEvent = new TileSelectEvent();
    tileSelectEvent.sessionId = this.player.sessionId;
    tileSelectEvent.playerId = this.player.id;
    tileSelectEvent.ticket = ticket;
    tileSelectEvent.row = row;
    tileSelectEvent.column = column;

    //Send the request to set the board
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/TILE-SELECT-REQUEST/${this.player.id}`, JSON.stringify(tileSelectEvent), `${this.topicHelper.prefix}/TILE-SELECT-REPLY/${this.player.id}/CONTROLLER`)
      .then((msg: any) => {
        let tileSelectResult: TileSelectResult = JSON.parse(msg.getBinaryAttachment());
        if (!tileSelectResult.success) {
          this.error = tileSelectResult.returnMessage;
        } else {
          this.tickets[ticket].ticketMatrix[row][column].isMarked = tileSelectResult.newIsMarked;
        }
      })
      .catch((err) => {
        this.error = err;
      });
  }

  showPrizeOptions() {
    this.showPrizes = true;
  }

  hidePrizeOptions() {
    this.showPrizes = false;
  }

  ticketModeOn(prizeIndex: number) {
    this.selectedPrizeIndex = prizeIndex;
    this.selectTicketMode = true;
  }

  ticketModeOff() {
    this.selectedPrizeIndex = null;
    this.selectTicketMode = false;
  }

  submitPrizeRequest(ticket: number) {
    let prizeSubmitEvent: PrizeSubmitEvent = new PrizeSubmitEvent();
    prizeSubmitEvent.sessionId = this.player.sessionId;
    prizeSubmitEvent.playerId = this.player.id;
    prizeSubmitEvent.ticket = ticket;
    prizeSubmitEvent.selectedPrizeIndex = this.selectedPrizeIndex;

    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/PRIZE-SUBMIT-REQUEST/${this.player.id}`, JSON.stringify(prizeSubmitEvent), `${this.topicHelper.prefix}/PRIZE-SUBMIT-REPLY/${this.player.id}/CONTROLLER`)
      .then((msg: any) => {
        let prizeSubmitResult: PrizeSubmitResult = JSON.parse(msg.getBinaryAttachment());
        console.log(prizeSubmitResult);
        console.log(prizeSubmitResult.responseType);
        if (prizeSubmitResult.responseType === "SUCCESS") {
          alert("You've been awarded " + this.prizes[prizeSubmitResult.selectedPrizeIndex].prizeName + ". Congrats!");
        } else if (prizeSubmitResult.responseType === "FAILURE") {
          this.player.ticketSet.tickets[prizeSubmitResult.ticket].isEliminated = true;
          alert(prizeSubmitResult.returnMessage);
        } else if (prizeSubmitResult.responseType === "ALREADY_TAKEN") {
          alert(prizeSubmitResult.returnMessage);
        } else {
          alert(prizeSubmitResult.returnMessage);
        }
      })
      .catch((err) => {
        this.error = err;
      });

    this.ticketModeOff();
  }

  detached() {
    //Unsubscribe from the .../GAMESTART-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`);
    //Unsubscribe from the .../NEXTNUMBER-CHOOSE-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`);
    //Unsubscribe from the .../UPDATE-PRIZE-STATUS event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/UPDATE-PRIZE-STATUS/CONTROLLER`);
    //Unsubscribe from the .../TILE-SELECT-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/TILE-SELECT-REPLY/CONTROLLER`);
    //Unsubscribe from the .../PRIZE-SUBMIT-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/PRIZE-SUBMIT-REPLY/CONTROLLER`);
  }
}
