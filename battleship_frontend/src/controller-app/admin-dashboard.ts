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

    //Subscribe to the UPDATE-PRIZE-STATUS event
    this.solaceClient.subscribe(
      `${this.topicPrefix}/UPDATE-PRIZE-STATUS/CONTROLLER`,
      // game start event handler callback
      (msg) => {
        let prizeStatusResult: PrizeStatusResult = JSON.parse(msg.getBinaryAttachment());

        if (prizeStatusResult != null) {
          this.prizes = prizeStatusResult.prizes;
          console.log(this.prizes);
          if (prizeStatusResult.isGameOver) {
            this.pageState = GAME_OVER_STATE;
          }
        }
      }
    );

    this.chooseNextNumberEvent();
    this.autoModeCaller();
    // this.startTimer();
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
    let nextNumberChooseEvent: NextNumberChooseEvent = new NextNumberChooseEvent();
    nextNumberChooseEvent.sessionId = this.sessionId;

    //Send the request to set the board
    this.solaceClient
      .sendRequest(`${this.topicPrefix}/NEXTNUMBER-CHOOSE-REQUEST`, JSON.stringify(nextNumberChooseEvent), `${this.topicPrefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let nextNumberChooseResult: NextNumberChooseResult = JSON.parse(msg.getBinaryAttachment());
        if (nextNumberChooseResult.success) {
          this.currentNumber = nextNumberChooseResult.value;
          this.gameNumberSet.numberSet[nextNumberChooseResult.rowIndex][nextNumberChooseResult.columnIndex].isMarked = true;
        }

        if (nextNumberChooseResult.isGameOver) {
          this.pageState = GAME_OVER_STATE;
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

      if (this.autoMode) {
        this.chooseNextNumberEvent();
      }
    }, 5000);
  }

  startTimer() {
    this.timerInterval = setInterval(() => {
      if (this.pageState === GAME_OVER_STATE) {
        return;
      }

      if (this.autoMode) {
        this.timePassed = this.timePassed += 1;
        this.timeLeft = this.TIME_LIMIT - this.timePassed;

        if (this.timeLeft == 0) {
          // this.chooseNextNumberEvent();
          // this.timeLeft = this.TIME_LIMIT;
          // this.timePassed = 0;
          document.getElementById("base-timer-path-remaining").setAttribute("stroke-dasharray", `0 283`);
        } else {
          this.setCircleDasharray();
        }
      }
    }, 1000);
  }

  // Divides time left by the defined time limit.
  calculateTimeFraction() {
    const rawTimeFraction = this.timeLeft / this.TIME_LIMIT;
    return rawTimeFraction - (1 / this.TIME_LIMIT) * (1 - rawTimeFraction);
  }

  // Update the dasharray value as time passes, starting with 283
  setCircleDasharray() {
    const circleDasharray = `${(this.calculateTimeFraction() * 283).toFixed(0)} 283`;
    // const circleDasharray = `0 283`;
    console.log(circleDasharray);
    document.getElementById("base-timer-path-remaining").setAttribute("stroke-dasharray", circleDasharray);
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
