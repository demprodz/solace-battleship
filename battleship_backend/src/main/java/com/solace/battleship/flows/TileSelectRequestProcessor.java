package com.solace.battleship.flows;

import com.solace.battleship.events.TileSelectRequest;
import com.solace.battleship.events.TileSelectResponseEvent;
import com.solace.battleship.flows.TileSelectRequestProcessor.TileSelectRequestProcessorBinding;
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
@EnableBinding(TileSelectRequestProcessorBinding.class)
public class TileSelectRequestProcessor extends AbstractRequestProcessor<TileSelectRequest> {

  // We define an INPUT to receive data from and dynamically specify the reply to
  // destination depending on the header and state of the game engine
  @StreamListener(TileSelectRequestProcessorBinding.INPUT)

  public void handle(TileSelectRequest tileSelectRequest, @Header("reply-to") String replyTo) {
    // Pass the request to make a move
    TileSelectResponseEvent result = gameEngine.requestToSelectTile(tileSelectRequest);
    // Send the result of the MoveRequest to the replyTo destination retrieved from
    // the message header
    resolver.resolveDestination(replyTo).send(message(result));
  }

  /*
   * Custom Processor Binding Interface to allow for multiple outputs
   */
  public interface TileSelectRequestProcessorBinding {
    String INPUT = "tile_select_request";

    @Input
    SubscribableChannel tile_select_request();
  }
}