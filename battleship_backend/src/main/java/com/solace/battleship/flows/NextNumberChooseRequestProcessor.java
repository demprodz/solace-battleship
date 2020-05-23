package com.solace.battleship.flows;

import com.solace.battleship.events.NextNumberChooseRequest;
import com.solace.battleship.events.NextNumberChooseResult;
import com.solace.battleship.flows.NextNumberChooseRequestProcessor.NextNumberChooseRequestProcessorBinding;
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
@EnableBinding(NextNumberChooseRequestProcessorBinding.class)
public class NextNumberChooseRequestProcessor extends AbstractRequestProcessor<NextNumberChooseRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(NextNumberChooseRequestProcessorBinding.INPUT)

  public void handle(NextNumberChooseRequest nextNumberChooseRequest, @Header("reply-to") String replyTo) {
    NextNumberChooseResult gameStart = gameEngine.getNextNumber(nextNumberChooseRequest.getSessionId());
    resolver.resolveDestination(replyTo).send(message(gameStart));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface NextNumberChooseRequestProcessorBinding {
    String INPUT = "nextnumber_choose_request";

    @Input
    SubscribableChannel nextnumber_choose_request();
  }
}