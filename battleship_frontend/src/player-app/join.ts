import { JoinResult } from "./../common/events";
import { Player, PlayerJoined, TopicHelper } from "../common/events";
import { inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";

/**
 * Class that represents the Join screen for the player
 * @author Thomas Kunnumpurath
 */
@inject(Router, SolaceClient, Player, TopicHelper)
export class Join {
  pageStatus = "Waiting for other player to join...";
  playerNickname: string = null;
  numTickets: number;
  playerId: string = Math.floor(Math.random() * 16777215).toString(16);

  constructor(private router: Router, private solaceClient: SolaceClient, private player: Player, private topicHelper: TopicHelper) {}

  /**
   * Aurelia function that is called once route is activated
   * @param params
   * @param routeConfig
   */
  activate(params, routeConfig) {
    this.player.id = this.playerId;
    this.player.sessionId = params.sessionId;

    //Connect to Solace
    if (this.solaceClient.session == null) {
      this.solaceClient
        .connect()
        .then(() => {
          //Warm up the subscription for the JOIN-REPLY
          this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/JOIN-REPLY/${this.player.id}/CONTROLLER`);
        })
        .catch((ex) => {
          console.log(ex);
        });
    } else {
      this.solaceClient.subscribeReply(`${this.topicHelper.prefix}/JOIN-REPLY/${this.player.id}/CONTROLLER`);
    }
  }

  /**
   * Function to join a game - asks for the Player's name before continuing
   */
  joinGame() {
    if (!this.playerNickname) {
      alert("Please enter a nickname before continuing");
      return;
    }

    if (!this.numTickets) {
      alert("Please enter number of tickets before continuing");
      return;
    }

    if (this.numTickets < 1 || this.numTickets > 6) {
      alert("Invalid number of tickets. You can play with 1 to 6 tickets.");
      return;
    }

    this.player.name = this.playerNickname;

    let playerJoined: PlayerJoined = new PlayerJoined();
    playerJoined.playerId = this.player.id;
    playerJoined.playerNickname = this.playerNickname;
    playerJoined.sessionId = this.player.sessionId;
    playerJoined.numTickets = this.numTickets;

    //Publish a join request and change the pageState to waiting if the join request succeeded
    let topicName: string = `${this.topicHelper.prefix}/JOIN-REQUEST/${this.player.id}`;
    let replyTopic: string = `${this.topicHelper.prefix}/JOIN-REPLY/${this.player.id}/CONTROLLER`;
    this.solaceClient
      .sendRequest(topicName, JSON.stringify(playerJoined), replyTopic)
      .then((msg: any) => {
        let joinResult: JoinResult = JSON.parse(msg.getBinaryAttachment());
        if (joinResult.success) {
          this.player.ticketSet = joinResult.ticketSet;
          this.player.numTickets = joinResult.ticketSet.tickets.length;
          this.router.navigateToRoute("housie-table", { playerId: this.player.id });
        } else this.pageStatus = "Join Request Failed - Player Already Joined!";
      })
      .catch((error) => {
        this.pageStatus = "Join Request Failed!";
      });
  }

  detached() {
    this.solaceClient.unsubscribe(`${this.topicHelper.prefix}/JOIN-REPLY/${this.player.id}/CONTROLLER`);
  }
}
