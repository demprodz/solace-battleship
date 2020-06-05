package com.solace.battleship.flows;

import com.solace.battleship.events.SessionCreateRequest;
import com.solace.battleship.events.SessionCreateResult;
import com.solace.battleship.flows.SessionCreateRequestProcessor.SessionCreateRequestProcessorBinding;
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
@EnableBinding(SessionCreateRequestProcessorBinding.class)
public class SessionCreateRequestProcessor extends AbstractRequestProcessor<SessionCreateRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(SessionCreateRequestProcessorBinding.INPUT)

  public void handle(SessionCreateRequest sessionCreateRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    SessionCreateResult result = gameEngine.requestToCreateSession(sessionCreateRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface SessionCreateRequestProcessorBinding {
    String INPUT = "session_create_request";

    @Input
    SubscribableChannel session_create_request();
  }
}