import { EventAggregator } from "aurelia-event-aggregator";
import { Router } from "aurelia-router";
import { GameParams } from "common/game-params";
import { MoveResponseEvent, TopicHelper, InternalMoveResult, GameStart, GameNumberSet, NextNumberChooseEvent, NextNumberChooseResult } from "../common/events";
import { bindable, inject } from "aurelia-framework";
import { SolaceClient } from "common/solace-client";
import { WAITING_STATE, IN_PROGRESS_STATE } from "./../common/constants";

import "./admin-dashboard.css";

@inject(SolaceClient, TopicHelper, GameParams, Router, EventAggregator, GameStart, GameNumberSet)
export class AdminDashboard {
  private error: string = "";
  private page_state: string = WAITING_STATE;
  private currentNumber: number;
  private autoMode: boolean = false;
  private isGameOver: boolean = false;

  constructor(
    private solaceClient: SolaceClient,
    private topicHelper: TopicHelper,
    private gameParams: GameParams,
    private router: Router,
    private ea: EventAggregator,
    private gameStart: GameStart,
    private gameNumberSet: GameNumberSet
  ) {
    console.log(this.winningPatternSet);
    //WARM-UP THE NEXTNUMBER-CHOOSE-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`);
  }

  /**
   * Submit request to choose the next random number
   */
  chooseNextNumberEvent() {
    let nextNumberChooseEvent: NextNumberChooseEvent = new NextNumberChooseEvent();
    nextNumberChooseEvent.sessionId = this.gameStart.sessionId;

    //Send the request to set the board
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/NEXTNUMBER-CHOOSE-REQUEST`, JSON.stringify(nextNumberChooseEvent), `${this.topicHelper.prefix}/NEXTNUMBER-CHOOSE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let nextNumberChooseResult: NextNumberChooseResult = JSON.parse(msg.getBinaryAttachment());
        if (nextNumberChooseResult.success) {
          this.currentNumber = nextNumberChooseResult.value;
          this.page_state = "";
          this.gameNumberSet.numberSet[nextNumberChooseResult.rowIndex][nextNumberChooseResult.columnIndex].isMarked = true;
          this.isGameOver = nextNumberChooseResult.isGameOver;
        } else {
          this.error = nextNumberChooseResult.returnMessage;
        }
      })
      .catch((err) => {
        this.error = err;
      });
  }

  toggleAutoMode() {
    this.autoMode = !this.autoMode;
  }

  attached() {
    this.chooseNextNumberEvent();

    this.autoModeCaller();
  }

  autoModeCaller() {
    setInterval(() => {
      if (this.isGameOver) {
        return;
      }

      if (this.autoMode) {
        this.chooseNextNumberEvent();
      }
    }, 5000);
  }

  detached() {
    // Unsubscribe from all MOVE-REPLY events
    // this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/MOVE-REPLY/*/*`);
  }
}
