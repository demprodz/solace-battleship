package com.solace.battleship.flows;

import com.solace.battleship.events.NextNumberConfirmRequest;
import com.solace.battleship.events.NextNumberConfirmResult;
import com.solace.battleship.flows.NextNumberConfirmRequestProcessor.NextNumberConfirmRequestProcessorBinding;
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
@EnableBinding(NextNumberConfirmRequestProcessorBinding.class)
public class NextNumberConfirmRequestProcessor extends AbstractRequestProcessor<NextNumberConfirmRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(NextNumberConfirmRequestProcessorBinding.INPUT)

  public void handle(NextNumberConfirmRequest nextNumberConfirmRequest, @Header("reply-to") String replyTo) {
    NextNumberConfirmResult nextNumberConfirmResult = gameEngine.requestToConfirmNextNumber(nextNumberConfirmRequest);
    resolver.resolveDestination(replyTo).send(message(nextNumberConfirmResult));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface NextNumberConfirmRequestProcessorBinding {
    String INPUT = "nextnumber_confirm_request";

    @Input
    SubscribableChannel nextnumber_confirm_request();
  }
}