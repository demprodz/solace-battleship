import { TOPIC_PREFIX } from "./../common/constants";
import {
  PlayerJoined,
  GameStart,
  TopicHelper,
  BoardSetEvent,
  MatchStart,
  BoardSetResult,
  GameStartRequest,
  GameNumberSet,
  JoinResult,
  SessionCreateRequest,
  SessionCreateResult,
} from "../common/events";
import { inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";
import { bindable } from "aurelia-framework";

/**
 * Class that represents a landing page
 */
@inject(Router, SolaceClient, TopicHelper, GameStart, GameNumberSet)
export class LandingPage {
  private player1Status: string = "Waiting for Player1 to Join...";
  private player2Status: string = "Waiting for Player2 to Join...";
  private playersMessage: string = "0 player(s) have joined.";
  private playerJoinUrl: string;
  private numPlayers: number;
  private playersList: string[] = [];
  private pageState: string = "Loading game...";
  private countdown: number = 0;

  //Generate a session-id for the game (a random hex string)
  sessionId: string = Math.floor(Math.random() * 16777215).toString(16);

  constructor(private router: Router, private solaceClient: SolaceClient, private topicHelper: TopicHelper, private gameStart: GameStart, private gameNumberSet: GameNumberSet) {
    this.playerJoinUrl = `http://${location.host}/player/${this.sessionId}/`;
    this.numPlayers = 0;
  }

  /**
   * Aurelia function that is called when the page is navigated to
   * @param params
   * @param routeConfig
   */
  activate(params, routeConfig) {
    this.topicHelper.prefix = TOPIC_PREFIX;

    // Connect to Solace
    if (this.solaceClient.session == null) {
      this.solaceClient.connect().then(() => {
        this.prepareSolaceSubscriptions();
      });
    } else {
      this.prepareSolaceSubscriptions();
    }
  }

  prepareSolaceSubscriptions() {
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/SESSION-CREATE-REPLY/${this.sessionId}/CONTROLLER`);

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
            this.playersMessage = ++this.numPlayers + " player(s) have joined.";
            this.playersList.push(joinResult.playerNickname);
          }
        }
      }
    );
  }

  createGame() {
    let sessionName: string = prompt("Enter a name for your new game:");

    if (sessionName == null) {
      return;
    }

    let sessionCreateRequest: SessionCreateRequest = new SessionCreateRequest();
    sessionCreateRequest.sessionId = this.sessionId;
    sessionCreateRequest.name = sessionName;
    sessionCreateRequest.playerJoinUrl = `http://${location.host}/player/${this.sessionId}/`;

    let topicName: string = `${this.topicHelper.prefix}/SESSION-CREATE-REQUEST`;
    let replyTopic: string = `${this.topicHelper.prefix}/SESSION-CREATE-REPLY/${this.sessionId}/CONTROLLER`;
    this.solaceClient
      .sendRequest(topicName, JSON.stringify(sessionCreateRequest), replyTopic)
      .then((msg: any) => {
        let sessionCreateResult: SessionCreateResult = JSON.parse(msg.getBinaryAttachment());

        if (sessionCreateResult.success) {
          this.router.navigateToRoute("admin", { sessionId: this.sessionId });
        }
      })
      .catch(() => {});
  }

  joinGame() {
    this.router.navigateToRoute("lobby");
  }

  detached() {
    //Unsubscribe from the ../JOIN-REQUEST/* event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/JOIN-REPLY/*/CONTROLLER`);
    //Unsubscribe from the ../GAME-START/CONTROLLER event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/SESSION-CREATE-REPLY/${this.sessionId}/CONTROLLER`);
  }
}
