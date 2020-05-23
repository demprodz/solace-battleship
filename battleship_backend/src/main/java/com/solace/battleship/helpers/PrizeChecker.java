package com.solace.battleship.helpers;

import com.solace.battleship.events.Player;
import com.solace.battleship.events.PrizeSubmitRequest;
import com.solace.battleship.models.GameNumberSet;
import com.solace.battleship.models.PrizeCheckerResponse;

public class PrizeChecker {
    public static PrizeCheckerResponse validatePrizeRequest(PrizeSubmitRequest request, Player player,
            GameNumberSet gameNumberSet) {
        if (gameNumberSet.getPrizes()[request.getSelectedPrizeIndex()].getIsTaken()) {
            return PrizeCheckerResponse.ALREADY_TAKEN;
        }

        if (gameNumberSet.getPrizes()[request.getSelectedPrizeIndex()]
                .checkPatternMatch(player.getTicketSet().getTicket(request.getTicket()), gameNumberSet)) {
            return PrizeCheckerResponse.SUCCESS;
        }
        return PrizeCheckerResponse.FAILURE;
    }
}