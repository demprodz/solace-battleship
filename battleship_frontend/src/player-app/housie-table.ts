import { TileSelectEvent, TileSelectResult, Ticket, NextNumberChooseResult, PrizeSubmitEvent, IPrize, PrizeSubmitResult } from "./../common/events";
import { WAITING_STATE, IN_PROGRESS_STATE } from "./../common/constants";
import { bindable, inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";
import { BoardSetEvent, Player, PlayerName, PrivateBoardCellState, KnownBoardCellState, TopicHelper, BoardSetResult, GameStart } from "../common/events";
import { GameParams } from "common/game-params";

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

  private tickets: Ticket[];
  private placedShips: number = 0;
  private countdown: number = 0;
  private donePlacing: boolean = false;
  private error: string = "";

  constructor(private router: Router, private solaceClient: SolaceClient, private player: Player, private gameParams: GameParams, private topicHelper: TopicHelper) {
    console.log(player);
    this.tickets = this.player.ticketSet.tickets;

    //WARM-UP THE TILE-SELECT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/TILE-SELECT-REPLY/${this.player.getPlayerIdForTopic()}/CONTROLLER`);

    //WARM-UP THE PRIZE-SUBMIT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/PRIZE-SUBMIT-REPLY/${this.player.getPlayerIdForTopic()}/CONTROLLER`);

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
        let prizes: IPrize[] = JSON.parse(msg.getBinaryAttachment());
        console.log(prizes);
        if (prizes != null) {
          this.prizes = prizes;
        }
      }
    );
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
      .sendRequest(
        `${this.topicHelper.prefix}/TILE-SELECT-REQUEST/${this.player.getPlayerIdForTopic()}`,
        JSON.stringify(tileSelectEvent),
        `${this.topicHelper.prefix}/TILE-SELECT-REPLY/${this.player.getPlayerIdForTopic()}/CONTROLLER`
      )
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
    console.log(ticket);

    let prizeSubmitEvent: PrizeSubmitEvent = new PrizeSubmitEvent();
    prizeSubmitEvent.sessionId = this.player.sessionId;
    prizeSubmitEvent.playerId = this.player.id;
    prizeSubmitEvent.ticket = ticket;
    prizeSubmitEvent.selectedPrizeIndex = this.selectedPrizeIndex;

    this.solaceClient
      .sendRequest(
        `${this.topicHelper.prefix}/PRIZE-SUBMIT-REQUEST/${this.player.getPlayerIdForTopic()}`,
        JSON.stringify(prizeSubmitEvent),
        `${this.topicHelper.prefix}/PRIZE-SUBMIT-REPLY/${this.player.getPlayerIdForTopic()}/CONTROLLER`
      )
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
    //Unsubscribe from the .../BOARD-SET-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`);
  }
}
