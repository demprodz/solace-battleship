package com.solace.battleship.flows;

import com.solace.battleship.events.PrizeSubmitRequest;
import com.solace.battleship.events.PrizeSubmitResult;
import com.solace.battleship.flows.PrizeSubmitRequestProcessor.PrizeSubmitRequestProcessorBinding;
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
@EnableBinding(PrizeSubmitRequestProcessorBinding.class)
public class PrizeSubmitRequestProcessor extends AbstractRequestProcessor<PrizeSubmitRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(PrizeSubmitRequestProcessorBinding.INPUT)

  public void handle(PrizeSubmitRequest prizeSubmitRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    PrizeSubmitResult result = gameEngine.requestToSubmitPrize(prizeSubmitRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));

    if (result.isSuccess()) {
      resolver
          .resolveDestination(
              "SOLACE/BATTLESHIP/" + prizeSubmitRequest.getSessionId() + "/UPDATE-PRIZE-STATUS/CONTROLLER")
          .send(message(gameEngine.getPrizeStatus(prizeSubmitRequest.getSessionId())));
    }

  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface PrizeSubmitRequestProcessorBinding {
    String INPUT = "prize_submit_request";

    @Input
    SubscribableChannel prize_submit_request();
  }
}