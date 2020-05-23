import { PlayerJoined, GameStart, TopicHelper, BoardSetEvent, MatchStart, BoardSetResult, GameStartRequest, GameNumberSet, JoinResult } from "../common/events";
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
  sessionId: string = "test"; //Math.floor(Math.random() * 16777215).toString(16);

  constructor(private router: Router, private solaceClient: SolaceClient, private topicHelper: TopicHelper, private gameStart: GameStart, private gameNumberSet: GameNumberSet) {
    //Append a session-id for the global topic prefix
    this.topicHelper.prefix = this.topicHelper.prefix + "/" + this.sessionId;
    this.playerJoinUrl = `http://${location.host}/join/${this.sessionId}/`;
    this.numPlayers = 0;
  }

  /**
   * Aurelia function that is called when the page is navigated to
   * @param params
   * @param routeConfig
   */
  activate(params, routeConfig) {
    // Connect to Solace
    this.solaceClient.connect().then(() => {
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
              this.playersMessage = ++this.numPlayers + " player(s) have joined.";
              this.playersList.push(joinResult.playerNickname);
            }
          }
        }
      );
    });
  }

  startGame() {
    let gameStartRequest: GameStartRequest = new GameStartRequest();
    // playerJoined.playerName = this.player.name;
    gameStartRequest.sessionId = this.sessionId;

    let topicName: string = `${this.topicHelper.prefix}/GAMESTART-REQUEST`;
    let replyTopic: string = `${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`;
    this.solaceClient
      .sendRequest(topicName, JSON.stringify(gameStartRequest), replyTopic)
      .then((msg: any) => {
        let gameStartReply: GameStart = JSON.parse(msg.getBinaryAttachment());

        if (gameStartReply.success) {
          this.pageState = "Game is beginning...";
          this.gameStart.sessionId = gameStartReply.sessionId;
          this.gameNumberSet.numberSet = gameStartReply.gameNumberSet.numberSet;
          this.gameNumberSet.prizes = gameStartReply.gameNumberSet.prizes;
          this.gameNumberSet.numbersLeft = gameStartReply.gameNumberSet.numbersLeft;

          this.loading(() => {
            this.router.navigateToRoute("admin-dashboard");
          });
        } else this.pageState = "Game failed to start!";
      })
      .catch((error) => {
        this.pageState = "Game failed to start!";
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

  detached() {
    //Unsubscribe from the ../JOIN-REQUEST/* event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/JOIN-REPLY/*/CONTROLLER`);
    //Unsubscribe from the ../GAME-START/CONTROLLER event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/GAMESTART-REPLY/CONTROLLER`);
  }
}
