package com.solace.battleship.flows;

import com.solace.battleship.events.PrizeModeRequest;
import com.solace.battleship.events.PrizeModeResult;
import com.solace.battleship.flows.PrizeModeOnRequestProcessor.PrizeModeOnRequestProcessorBinding;
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
@EnableBinding(PrizeModeOnRequestProcessorBinding.class)
public class PrizeModeOnRequestProcessor extends AbstractRequestProcessor<PrizeModeRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(PrizeModeOnRequestProcessorBinding.INPUT)

  public void handle(PrizeModeRequest prizeModeOnRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    PrizeModeResult result = gameEngine.requestToTurnPrizeModeOn(prizeModeOnRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface PrizeModeOnRequestProcessorBinding {
    String INPUT = "prizemode_on_request";

    @Input
    SubscribableChannel prizemode_on_request();
  }
}