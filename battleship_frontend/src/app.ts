import { TOPIC_PREFIX } from "./common/constants";
import { TopicHelper } from "./common/events";
import { gameConfig } from "./common/game-config";
import { PLATFORM } from "aurelia-pal";
import { inject } from "aurelia-framework";
import { GameParams } from "common/game-params";
import "./css/style.css";

/**
 * Aurelia Router Object - this object sets the paths for the various pages in the app.
 * @author Thomas Kunnumpurath, Andrew Roberts
 */
@inject(GameParams, TopicHelper)
export class App {
  router: any;

  constructor(gameParams: GameParams, topicHelper: TopicHelper) {
    //Initializing the game params
    gameParams.allowedShips = gameConfig.allowed_ships;
    gameParams.gameboardDimensions = gameConfig.gameboard_dimensions;
    gameParams.numRows = gameConfig.numRows;
    gameParams.numColumns = gameConfig.numColumns;
    //Initializing the TopicPrefix with
    topicHelper.prefix = TOPIC_PREFIX;
  }

  configureRouter(config, router) {
    config.title = "Housie";
    config.options.pushState = true; // No # in URL
    config.map([
      { route: "/", moduleId: PLATFORM.moduleName("controller-app/landing-page"), name: "" },
      { route: "/lobby", moduleId: PLATFORM.moduleName("player-app/lobby"), name: "lobby" },
      { route: "/admin/:sessionId", moduleId: PLATFORM.moduleName("./admin-view"), name: "admin" },
      { route: "/player/:sessionId", moduleId: PLATFORM.moduleName("./player-view"), name: "player" },
    ]);
    this.router = router;
  }

  goHome() {
    window.location.href = "/";
  }

  attached() {
    //Load the particlesJS library by appending an HTML element to the end of the Body - its the only way to load it in
    let script = document.createElement("script");
    script.type = "text/javascript";
    script.innerHTML = 'particlesJS.load("particles-js", "particles.json", null);';
    document.querySelector("body").appendChild(script);
  }
}
