package com.solace.battleship.flows;

import com.solace.battleship.events.AdminPageReloadResult;
import com.solace.battleship.events.AdminPageRequest;
import com.solace.battleship.flows.AdminPageRequestProcessor.AdminPageRequestProcessorBinding;
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
@EnableBinding(AdminPageRequestProcessorBinding.class)
public class AdminPageRequestProcessor extends AbstractRequestProcessor<AdminPageRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(AdminPageRequestProcessorBinding.INPUT)
  public void handle(AdminPageRequest adminPageRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    AdminPageReloadResult result = gameEngine.requestToGetAdminPageReload(adminPageRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface AdminPageRequestProcessorBinding {
    String INPUT = "admin_page_request";

    @Input
    SubscribableChannel admin_page_request();
  }
}