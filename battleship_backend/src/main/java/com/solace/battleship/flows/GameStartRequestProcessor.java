package com.solace.battleship.flows;

import com.solace.battleship.events.GameStart;
import com.solace.battleship.events.GameStartRequest;
import com.solace.battleship.flows.GameStartRequestProcessor.GameStartRequestProcessorBinding;
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
@EnableBinding(GameStartRequestProcessorBinding.class)
public class GameStartRequestProcessor extends AbstractRequestProcessor<GameStartRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(GameStartRequestProcessorBinding.INPUT)

  public void handle(GameStartRequest gameStartRequest, @Header("reply-to") String replyTo) {
    GameStart gameStart = gameEngine.getGameStartAndStartGame(gameStartRequest.getSessionId());
    resolver.resolveDestination(replyTo).send(message(gameStart));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface GameStartRequestProcessorBinding {
    String INPUT = "gamestart_request";

    @Input
    SubscribableChannel gamestart_request();
  }
}