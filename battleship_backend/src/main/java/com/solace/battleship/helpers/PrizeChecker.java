package com.solace.battleship.helpers;

import com.solace.battleship.events.Player;
import com.solace.battleship.events.PrizeSubmitRequest;
import com.solace.battleship.models.GameNumberSet;
import com.solace.battleship.models.IPrize;
import com.solace.battleship.models.PrizeCheckerResponse;
import com.solace.battleship.models.Ticket;

public class PrizeChecker {
    public static PrizeCheckerResponse validatePrizeRequest(PrizeSubmitRequest request, Player player,
            GameNumberSet gameNumberSet, boolean isOverride) {
        IPrize prize = gameNumberSet.getPrizes()[request.getSelectedPrizeIndex()];

        if (prize.getIsTaken()) {
            return PrizeCheckerResponse.ALREADY_TAKEN;
        }

        Ticket ticket = player.getTicketSet().getTicket(request.getTicket());

        if (ticket.prizeIsClaimed(prize.getPrizeName())) {
            return PrizeCheckerResponse.USER_ALREADY_TAKEN;
        }

        if (isOverride || prize.checkPatternMatch(player, ticket, gameNumberSet)) {
            prize.addWinner(player.getName());
            ticket.addClaimedPrize(prize.getPrizeName());
            return PrizeCheckerResponse.SUCCESS;
        }

        System.out.println("Prize Submit Failure - details below...");
        System.out.println(prize.getPrizeName());
        gameNumberSet.printMarkedNumbers();
        ticket.printMarkedSpots();
        return PrizeCheckerResponse.FAILURE;
    }
}