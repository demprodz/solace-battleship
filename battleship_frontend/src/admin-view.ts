import { Player, TopicHelper } from "./common/events";
import { PLATFORM } from "aurelia-pal";
import { inject } from "aurelia-framework";
import { Router } from "aurelia-router";
import { SolaceClient } from "common/solace-client";

/**
 * Aurelia Router Object - this object sets the paths for the various pages in the app.
 * @author Thomas Kunnumpurath, Andrew Roberts
 */
@inject(Router, SolaceClient, Player, TopicHelper)
export class AdminView {
  constructor(private router: Router, private solaceClient: SolaceClient, private player: Player, private topicHelper: TopicHelper) {}

  activate(params, routeConfig) {
    this.topicHelper.prefix = this.topicHelper.prefix + "/" + params.sessionId;
  }

  configureRouter(config, router) {
    config.map([
      {
        route: "/",
        moduleId: PLATFORM.moduleName("controller-app/admin-landing-page"),
        name: "join",
      },
      {
        route: "/admin-dashboard/",
        moduleId: PLATFORM.moduleName("controller-app/admin-dashboard"),
        name: "admin-dashboard",
      },
    ]);
    this.router = router;
  }

  // attached() {
  //   //Load the particlesJS library by appending an HTML element to the end of the Body - its the only way to load it in
  //   let script = document.createElement("script");
  //   script.type = "text/javascript";
  //   script.innerHTML = 'particlesJS.load("particles-js", "particles.json", null);';
  //   document.querySelector("body").appendChild(script);
  // }
}
