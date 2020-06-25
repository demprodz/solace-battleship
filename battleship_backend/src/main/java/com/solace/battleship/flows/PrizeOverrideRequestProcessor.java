package com.solace.battleship.flows;

import com.solace.battleship.events.PrizeSubmitRequest;
import com.solace.battleship.events.PrizeSubmitResult;
import com.solace.battleship.flows.PrizeOverrideRequestProcessor.PrizeOverrideRequestProcessorBinding;
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
@EnableBinding(PrizeOverrideRequestProcessorBinding.class)
public class PrizeOverrideRequestProcessor extends AbstractRequestProcessor<PrizeSubmitRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(PrizeOverrideRequestProcessorBinding.INPUT)

  public void handle(PrizeSubmitRequest prizeOverrideRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    PrizeSubmitResult result = prizeOverrideRequest.getIsConfirmedDenial()
        ? gameEngine.requestToConfirmDeniedPrize(prizeOverrideRequest)
        : gameEngine.requestToOverridePrize(prizeOverrideRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved
    // from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));

    if (result.isSuccess()) {
      resolver
          .resolveDestination(
              "SOLACE/BATTLESHIP/" + prizeOverrideRequest.getSessionId() + "/UPDATE-PRIZE-STATUS/CONTROLLER")
          .send(message(gameEngine.getPrizeStatus(prizeOverrideRequest.getSessionId())));
    }

  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface PrizeOverrideRequestProcessorBinding {
    String INPUT = "prize_override_request";

    @Input
    SubscribableChannel prize_override_request();
  }
}