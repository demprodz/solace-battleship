package com.solace.battleship.flows;

import com.solace.battleship.events.AdminLandingPageReloadResult;
import com.solace.battleship.events.AdminLandingPageRequest;
import com.solace.battleship.events.AdminPageReloadResult;
import com.solace.battleship.events.AdminPageRequest;
import com.solace.battleship.flows.AdminLandingPageRequestProcessor.AdminLandingPageRequestProcessorBinding;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.handler.annotation.Header;

/**
 * This Spring Cloud Stream processor handles join-requests for the Battleship
 * Game
 *
 * @author Thomas Kunnumpurath
 */
@SpringBootApplication
@EnableBinding(AdminLandingPageRequestProcessorBinding.class)
public class AdminLandingPageRequestProcessor extends AbstractRequestProcessor<AdminLandingPageRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(AdminLandingPageRequestProcessorBinding.INPUT)
  public void handle(AdminLandingPageRequest adminLandingPageRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    AdminLandingPageReloadResult result = gameEngine.requestToGetAdminLandingPageReload(adminLandingPageRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface AdminLandingPageRequestProcessorBinding {
    String INPUT = "admin_landingpage_request";

    @Input
    SubscribableChannel admin_landingpage_request();
  }
}