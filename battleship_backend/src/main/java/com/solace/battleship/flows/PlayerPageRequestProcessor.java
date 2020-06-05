package com.solace.battleship.flows;

import com.solace.battleship.events.PlayerPageRequest;
import com.solace.battleship.events.PlayerPageReloadResult;
import com.solace.battleship.flows.PlayerPageRequestProcessor.PlayerPageRequestProcessorBinding;
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
@EnableBinding(PlayerPageRequestProcessorBinding.class)
public class PlayerPageRequestProcessor extends AbstractRequestProcessor<PlayerPageRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(PlayerPageRequestProcessorBinding.INPUT)

  public void handle(PlayerPageRequest playerPageRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    PlayerPageReloadResult result = gameEngine.requestToGetPlayerPageReload(playerPageRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface PlayerPageRequestProcessorBinding {
    String INPUT = "player_page_request";

    @Input
    SubscribableChannel player_page_request();
  }
}