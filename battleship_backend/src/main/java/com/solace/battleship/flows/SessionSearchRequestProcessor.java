package com.solace.battleship.flows;

import com.solace.battleship.events.SessionCreateRequest;
import com.solace.battleship.events.SessionCreateResult;
import com.solace.battleship.events.SessionSearchRequest;
import com.solace.battleship.events.SessionSearchResult;
import com.solace.battleship.flows.SessionSearchRequestProcessor.SessionSearchRequestProcessorBinding;
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
@EnableBinding(SessionSearchRequestProcessorBinding.class)
public class SessionSearchRequestProcessor extends AbstractRequestProcessor<SessionSearchRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(SessionSearchRequestProcessorBinding.INPUT)

  public void handle(SessionSearchRequest sessionSearchRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    SessionSearchResult result = gameEngine.requestToSearchSession(sessionSearchRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface SessionSearchRequestProcessorBinding {
    String INPUT = "session_search_request";

    @Input
    SubscribableChannel session_search_request();
  }
}