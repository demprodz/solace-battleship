import { TopicHelper, Session, SessionCreateResult, LobbyPageRequest, LobbyPageReloadResult, SessionSummary, SearchSessionRequest, SearchSessionResult } from "../common/events";
import { inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";
import "./lobby.css";

/**
 * Class that represents the Join screen for the player
 * @author Thomas Kunnumpurath
 */
@inject(Router, SolaceClient, TopicHelper)
export class Lobby {
  private games: Map<String, SessionSummary> = new Map<String, SessionSummary>();

  private searchSessionCode: string = "";

  constructor(private router: Router, private solaceClient: SolaceClient, private topicHelper: TopicHelper) {}

  /**
   * Aurelia function that is called once route is activated
   * @param params
   * @param routeConfig
   */
  activate(params, routeConfig) {
    if (this.solaceClient.session == null) {
      //Connect to Solace
      this.solaceClient.connect().then(() => {
        this.reloadLobbyPageFromServer();
      });
    } else {
      this.reloadLobbyPageFromServer();
    }
  }

  prepareSolaceSubscriptions() {
    //Listener for join replies from the battleship-server
    this.solaceClient.subscribe(
      `${this.topicHelper.prefix}/SESSION-CREATE-REPLY/*/CONTROLLER`,
      // join event handler callback
      (msg) => {
        if (msg.getBinaryAttachment()) {
          // parse received event
          let sessionCreateResult: SessionCreateResult = JSON.parse(msg.getBinaryAttachment());

          if (sessionCreateResult.success) {
            let sessionSummary: SessionSummary = sessionCreateResult.sessionSummary;
            sessionSummary.numPlayers = Object.keys(sessionSummary.players).length;
            this.games.set(sessionSummary.sessionId, sessionSummary);
          }
        }
      }
    );
  }

  reloadLobbyPageFromServer() {
    let lobbyPageRequest: LobbyPageRequest = new LobbyPageRequest();

    //WARM-UP THE LOBBY-PAGE-REPLY SUBSCRIPTION
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/LOBBY-PAGE-REPLY/CONTROLLER`);

    //Send the request to get player status (upon refresh)
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/LOBBY-PAGE-REQUEST`, JSON.stringify(lobbyPageRequest), `${this.topicHelper.prefix}/LOBBY-PAGE-REPLY/CONTROLLER`)
      .then((msg: any) => {
        let lobbyPageReloadResult: LobbyPageReloadResult = JSON.parse(msg.getBinaryAttachment());

        if (lobbyPageReloadResult.success) {
          this.copySessionsIntoMap(lobbyPageReloadResult.sessions);
        }
        this.prepareSolaceSubscriptions();
      })
      .catch(() => {});
  }

  copySessionsIntoMap(sessions: Map<String, SessionSummary>) {
    this.games = new Map<String, SessionSummary>();

    for (var i in sessions) {
      sessions[i].numPlayers = Object.keys(sessions[i].players).length;
      this.games.set(i, sessions[i]);
    }
  }

  searchGame() {
    //Warm up the subscription for the JOINSESSION-REPLY
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/SESSION-SEARCH-REPLY/${this.searchSessionCode}/CONTROLLER`);

    let searchSessionRequest: SearchSessionRequest = new SearchSessionRequest();
    searchSessionRequest.sessionId = this.searchSessionCode;

    // Send request to search for existing session
    this.solaceClient
      .sendRequest(`${this.topicHelper.prefix}/SESSION-SEARCH-REQUEST`, JSON.stringify(searchSessionRequest), `${this.topicHelper.prefix}/SESSION-SEARCH-REPLY/${this.searchSessionCode}/CONTROLLER`)
      .then((msg: any) => {
        let searchSessionResult: SearchSessionResult = JSON.parse(msg.getBinaryAttachment());

        if (searchSessionResult.success) {
          this.router.navigateToRoute("player", { sessionId: searchSessionResult.sessionId });
        } else {
          alert(searchSessionResult.errorMessage);
        }
      })
      .catch(() => {});
  }

  joinGame(key: string) {
    this.router.navigateToRoute("player", { sessionId: this.games.get(key).sessionId });
  }

  detached() {
    //Unsubscribe from the .../LOBBY-PAGE-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/LOBBY-PAGE-REPLY/CONTROLLER`);
    //Unsubscribe from the .../SESSION-CREATE-REPLY event
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/SESSION-CREATE-REPLY/*/CONTROLLER`);
    //Unsubscribe from the .../SESSION-SEARCH-REPLY event
    this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/SESSION-SEARCH-REPLY/*/CONTROLLER`);
  }
}
