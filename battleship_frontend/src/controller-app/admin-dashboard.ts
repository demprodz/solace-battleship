import { EventAggregator } from "aurelia-event-aggregator";
import { Router } from "aurelia-router";
import { GameParams } from "common/game-params";
import {
  MoveResponseEvent,
  TopicHelper,
  InternalMoveResult,
  GameStart,
  GameNumberSet,
  NextNumberChooseEvent,
  NextNumberChooseResult,
  IPrize,
  PrizeStatusResult,
  AdminDashboardPageRequest,
  AdminDashboardPageReloadResult,
  NextNumberConfirmEvent,
  NextNumberConfirmResult,
  PrizeModeResult,
} from "../common/events";
import { bindable, inject } from "aurelia-framework";
import { SolaceClient } from "common/solace-client";
import { WAITING_STATE, IN_PROGRESS_STATE, GAME_OVER_STATE } from "./../common/constants";

import "./admin-dashboard.css";

@inject(SolaceClient, TopicHelper, GameParams, Router, EventAggregator, GameStart, GameNumberSet)
export class AdminDashboard {
  private error: string = "";
  private pageState: string = IN_PROGRESS_STATE;
  private currentNumber: number;
  private autoMode: boolean = false;
  private prizeOnMode: boolean = false;
  private timer: number = 10;
  private sessionId: string;
  private topicPrefix: string;

  @bindable
  private prizes: IPrize[];

  private timerInterval;

  private COLOR_CODES = {
    info: {
      color: "green",
    },
  };

  private remainingPathColor = this.COLOR_CODES.info.color;

  // Start with an initial value of 20 seconds
  private TIME_LIMIT: number = 10;

  // Initially, no time has passed, but this will count up
  // and subtract from the TIME_LIMIT
  private timePassed: number = 0;
  private timeLeft: number = this.TIME_LIMIT;

  constructor(
    private solaceClient: SolaceClient,
    private topicHelper: TopicHelper,
    private gameParams: GameParams,
    private router: Router,
    private ea: EventAggregator,
    private gameStart: GameStart,
    private gameNumberSet: GameNumberSet
  ) {
    this.prizes = this.gameNumberSet.prizes;
    this.timer = this.gameStart.timer;
    this.autoMode = this.gameStart.isAutoMode;
  }

  activate(params, routeConfig) {
    this.topicPrefix = this.topicHelper.prefix;
    this.sessionId = params.sessionId;

    if (this.solaceClient.session == null) {
      //Connect to Solace
      this.solaceClient.connect().then(() => {
        this.reloadAdminPageFromServer();
      });
    } else {
      this.prepareSolaceSubscriptions();
    }
  }

  prepareSolaceSubscriptions() {
    //WARM-UP THE NEXTNUMBER-CHOOSE-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicPrefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`);

    //WARM-UP THE NEXTNUMBER-CONFIRM-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicPrefix}/NEXTNUMBER-CONFIRM-REPLY/CONTROLLER`);

    //Subscribe to the UPDATE-PRIZE-STATUS event
    this.solaceClient.subscribe(
      `${this.topicPrefix}/UPDATE-PRIZE-STATUS/CONTROLLER`,
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

    //Subscribe to the PRIZEMODE-ON-REPLY event
    this.solaceClient.subscribe(
      `${this.topicPrefix}/PRIZEMODE-REPLY/CONTROLLER`,
      // game start event handler callback
      (msg) => {
        let prizeModeResult: PrizeModeResult = JSON.parse(msg.getBinaryAttachment());
        console.log(prizeModeResult);
        if (prizeModeResult.success) {
          this.prizeOnMode = prizeModeResult.numPrizeModePlayers > 0;
        }
      }
    );

    this.chooseNextNumberEvent();
    this.autoModeCaller();
  }

  reloadAdminPageFromServer() {
    let adminDashboardPageRequest: AdminDashboardPageRequest = new AdminDashboardPageRequest();
    adminDashboardPageRequest.sessionId = this.sessionId;

    //WARM-UP THE PRIZE-SUBMIT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicPrefix}/ADMIN-PAGE-REPLY/CONTROLLER`);

    //Send the request to get player status (upon refresh)
    this.solaceClient
      .sendRequest(`${this.topicPrefix}/ADMIN-PAGE-REQUEST`, JSON.stringify(adminDashboardPageRequest), `${this.topicPrefix}/ADMIN-PAGE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let adminDashboardPageReloadResult: AdminDashboardPageReloadResult = JSON.parse(msg.getBinaryAttachment());
        if (adminDashboardPageReloadResult.success) {
          this.gameNumberSet = adminDashboardPageReloadResult.gameNumberSet;
          this.pageState = adminDashboardPageReloadResult.isGameInProgress ? IN_PROGRESS_STATE : GAME_OVER_STATE;
          this.prizes = adminDashboardPageReloadResult.prizes;
          this.timer = adminDashboardPageReloadResult.timer;
          this.autoMode = false;
          this.prizeOnMode = adminDashboardPageReloadResult.numPrizeModePlayers > 0;
        }

        this.prepareSolaceSubscriptions();
      })
      .catch((err) => {
        this.error = err;
      });
  }

  /**
   * Submit request to choose the next random number
   */
  chooseNextNumberEvent() {
    if (this.prizeOnMode === true) {
      return;
    }

    let nextNumberChooseEvent: NextNumberChooseEvent = new NextNumberChooseEvent();
    nextNumberChooseEvent.sessionId = this.sessionId;

    //Send the request to set the board
    this.solaceClient
      .sendRequest(`${this.topicPrefix}/NEXTNUMBER-CHOOSE-REQUEST`, JSON.stringify(nextNumberChooseEvent), `${this.topicPrefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let nextNumberChooseResult: NextNumberChooseResult = JSON.parse(msg.getBinaryAttachment());
        if (nextNumberChooseResult.success) {
          this.currentNumber = nextNumberChooseResult.value;
          this.gameNumberSet.numberSet[nextNumberChooseResult.rowIndex][nextNumberChooseResult.columnIndex].isHardMarked = true;

          let nextNumberConfirmEvent: NextNumberConfirmEvent = new NextNumberConfirmEvent();
          nextNumberConfirmEvent.sessionId = this.sessionId;
          nextNumberConfirmEvent.rowIndex = nextNumberChooseResult.rowIndex;
          nextNumberConfirmEvent.columnIndex = nextNumberChooseResult.columnIndex;

          this.solaceClient
            .sendRequest(`${this.topicPrefix}/NEXTNUMBER-CONFIRM-REQUEST`, JSON.stringify(nextNumberConfirmEvent), `${this.topicPrefix}/NEXTNUMBER-CONFIRM-REPLY/CONTROLLER`)
            .then((msg: any) => {
              let nextNumberConfirmResult: NextNumberConfirmResult = JSON.parse(msg.getBinaryAttachment());

              if (nextNumberConfirmResult.isGameOver) {
                this.pageState = GAME_OVER_STATE;
              }
            });
        }
      })
      .catch((err) => {
        this.error = err;
      });
  }

  toggleAutoMode() {
    this.autoMode = !this.autoMode;
  }

  autoModeCaller() {
    setInterval(() => {
      if (this.pageState === GAME_OVER_STATE) {
        return;
      }

      if (this.autoMode && !this.prizeOnMode) {
        this.chooseNextNumberEvent();
      }
    }, this.timer * 1000);
  }

  detached() {
    // Unsubscribe from all UPDATE-PRIZE-STATUS events
    this.solaceClient.unsubscribe(`${this.topicPrefix}/UPDATE-PRIZE-STATUS/CONTROLLER`);
    // Unsubscribe from all NEXTNUMBER-CHOOSE-REPLY events
    this.solaceClient.unsubscribe(`${this.topicPrefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`);
    // Unsubscribe from all ADMIN-PAGE-REPLY events
    this.solaceClient.unsubscribe(`${this.topicPrefix}/ADMIN-PAGE-REPLY/CONTROLLER`);
  }
}
