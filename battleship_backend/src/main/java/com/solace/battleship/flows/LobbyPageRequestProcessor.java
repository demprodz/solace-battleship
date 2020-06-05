package com.solace.battleship.flows;

import com.solace.battleship.events.AdminPageReloadResult;
import com.solace.battleship.events.AdminPageRequest;
import com.solace.battleship.events.LobbyPageReloadResult;
import com.solace.battleship.events.LobbyPageRequest;
import com.solace.battleship.flows.LobbyPageRequestProcessor.LobbyPageRequestProcessorBinding;
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
@EnableBinding(LobbyPageRequestProcessorBinding.class)
public class LobbyPageRequestProcessor extends AbstractRequestProcessor<LobbyPageRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(LobbyPageRequestProcessorBinding.INPUT)
  public void handle(LobbyPageRequest lobbyPageRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    LobbyPageReloadResult result = gameEngine.requestToGetLobbyPageReload(lobbyPageRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface LobbyPageRequestProcessorBinding {
    String INPUT = "lobby_page_request";

    @Input
    SubscribableChannel lobby_page_request();
  }
}