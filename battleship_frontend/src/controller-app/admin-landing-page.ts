import { GameStart, TopicHelper, GameStartRequest, GameNumberSet, JoinResult, AdminLandingPageRequest, AdminLandingPageReloadResult, SessionSummary, IPrize } from "../common/events";
import { inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";
import { bindable } from "aurelia-framework";
import "./admin-landing-page.css";

/**
 * Class that represents a landing page
 */
@inject(Router, SolaceClient, TopicHelper, GameStart, GameNumberSet)
export class AdminLandingPage {
  private playersMessage: string = "player(s) have joined.";
  private playerJoinUrl: string;

  @bindable
  private playersList: string[] = [];

  @bindable
  private sessionSummary: SessionSummary;

  private isAutoMode: boolean = false;
  private timers: string[] = ["5 seconds", "10 seconds", "30 seconds", "45 seconds", "60 seconds"];
  private selectedTimer: string = "";

  private pageState: string = "Loading game...";
  private sessionId: string;
  private copyToClipboardMessage: string = "Copy URL";
  private error: string;

  constructor(private router: Router, private solaceClient: SolaceClient, private topicHelper: TopicHelper, private gameStart: GameStart, private gameNumberSet: GameNumberSet) {}

  /**
   * Aurelia function that is called when the page is navigated to
   * @param params
   * @param routeConfig
   */
  activate(params, routeConfig) {
    this.sessionId = params.sessionId;
    this.playerJoinUrl = `http://${location.host}/player/${this.sessionId}/`;

    if (this.solaceClient.session == null) {
      //Connect to Solace
      this.solaceClient.connect().then(() => {
        this.reloadAdminLandingPageFromServer();
      });
    } else {
      this.reloadAdminLandingPageFromServer();
    }
  }

  prepareSolaceSubscriptions() {
    //Warm up the subscription for the GAMESTART-REPLY
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`);

    //Listener for join replies from the battleship-server
    this.solaceClient.subscribe(
      `${this.topicHelper.prefix}/JOIN-REPLY/*/CONTROLLER`,
      // join event handler callback
      (msg) => {
        if (msg.getBinaryAttachment()) {
          // parse received event
          let joinResult: JoinResult = JSON.parse(msg.getBinaryAttachment());
          if (joinResult.success) {
            // update client statuses
            this.playersList.push(joinResult.playerNickname);
          }
        }
      }
    );
  }

  reloadAdminLandingPageFromServer() {
    let adminLandingPageRequest: AdminLandingPageRequest = new AdminLandingPageRequest();
    adminLandingPageRequest.sessionId = this.sessionId;

    //WARM-UP THE PRIZE-SUBMIT-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/ADMIN-LANDINGPAGE-REPLY/CONTROLLER`);

    //Send the request to get player status (upon refresh)
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/ADMIN-LANDINGPAGE-REQUEST`, JSON.stringify(adminLandingPageRequest), `${this.topicHelper.prefix}/ADMIN-LANDINGPAGE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let adminLandingPageReloadResult: AdminLandingPageReloadResult = JSON.parse(msg.getBinaryAttachment());

        if (adminLandingPageReloadResult.success) {
          this.sessionSummary = adminLandingPageReloadResult.sessionSummary;
          this.parsePlayersList(adminLandingPageReloadResult.sessionSummary.players);
        }

        this.prepareSolaceSubscriptions();
      })
      .catch((err) => {
        this.error = err;
      });
  }

  parsePlayersList(players: {}) {
    this.playersList = [];

    for (var key in players) {
      this.playersList.push(players[key].name);
    }
  }

  startGame() {
    let gameStartRequest: GameStartRequest = new GameStartRequest();
    gameStartRequest.sessionId = this.sessionId;
    gameStartRequest.isAutoMode = this.isAutoMode;
    gameStartRequest.selectedTimer = this.selectedTimer;

    let disabledPrizesIndexList = [];
    for (var i in this.sessionSummary.prizes) {
      if (this.sessionSummary.prizes[i].isEnabled !== true) {
        disabledPrizesIndexList.push(i);
      }
    }
    gameStartRequest.disabledPrizesIndexList = disabledPrizesIndexList;

    let topicName: string = `${this.topicHelper.prefix}/GAMESTART-REQUEST`;
    let replyTopic: string = `${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`;
    this.solaceClient
      .sendRequest(topicName, JSON.stringify(gameStartRequest), replyTopic)
      .then((msg: any) => {
        let gameStartReply: GameStart = JSON.parse(msg.getBinaryAttachment());

        if (gameStartReply.success) {
          this.pageState = "Game is beginning...";
          this.gameStart.sessionId = gameStartReply.sessionId;
          this.gameStart.isAutoMode = gameStartReply.isAutoMode;
          this.gameStart.timer = gameStartReply.timer;

          this.gameNumberSet.numberSet = gameStartReply.gameNumberSet.numberSet;
          this.gameNumberSet.prizes = gameStartReply.gameNumberSet.prizes;
          this.gameNumberSet.numbersLeft = gameStartReply.gameNumberSet.numbersLeft;

          this.router.navigateToRoute("admin-dashboard");
        } else this.pageState = "Game failed to start!";
      })
      .catch((error) => {
        this.pageState = "Game failed to start!";
      });
  }

  copyUrlToClipboard() {
    const input = document.createElement("input");
    document.body.appendChild(input);

    input.value = this.playerJoinUrl;

    input.focus();
    input.select();

    const isSuccessful = document.execCommand("copy");

    if (!isSuccessful) {
      console.error("Failed to copy text.");
    } else {
      this.copyToClipboardMessage = "Copied URL";
      input.style.visibility = "hidden";
    }
  }

  togglePrize(prizeIndex: number) {
    this.sessionSummary.prizes[prizeIndex].isEnabled = !this.sessionSummary.prizes[prizeIndex].isEnabled;
  }

  detached() {
    //Unsubscribe from the ../JOIN-REQUEST/* event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/JOIN-REPLY/*/CONTROLLER`);
    //Unsubscribe from the ../GAMESTART-REPLY/CONTROLLER event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`);
    //Unsubscribe from the ../ADMIN-LANDINGPAGE-REPLY/CONTROLLER event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/ADMIN-LANDINGPAGE-REPLY/CONTROLLER`);
  }
}
