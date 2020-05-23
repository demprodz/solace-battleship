package com.solace.battleship.helpers;

import java.util.ArrayList;
import java.util.Random;

import com.solace.battleship.models.PlayerNumberTable;
import com.solace.battleship.models.Ticket;
import com.solace.battleship.models.TicketSet;

public class TicketGenerator {
    private static int[][] ticketSheet;

    private static final Random random = new Random();

    private static final int rows = 3;
    private static final int columns = 9;

    private static int[] maxConsecutiveNullRowSpots = { 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2 };
    private static int[] totalColumnSpots = { 9, 10, 10, 10, 10, 10, 10, 10, 11 };

    public static TicketSet createTicketSet(int numTickets) {
        do {
            createBlankTickets();
        } while (!isValidTicketsLayout());

        populateTicketSheet();

        return distributeTickets(numTickets);
    }

    private static void createBlankTickets() {
        ticketSheet = new int[rows * 6][columns];

        int[] consecutivePopulatedRowSpotsCount = new int[18];
        int[] totalPopulatedRowSpotsCount = new int[18];
        int[] remainingColumnSpots = totalColumnSpots.clone();

        for (int column = 0; column < columns; column++) {
            boolean isIndividualTicketColumnPopulated = false;
            for (int row = 0; row < ticketSheet.length; row++) {
                if (!isIndividualTicketColumnPopulated && totalPopulatedRowSpotsCount[row] < 5
                        && consecutivePopulatedRowSpotsCount[row] < 2 && (row % 3 == 2 || random.nextInt(2) == 1)) {
                    ticketSheet[row][column] = 1;
                    totalPopulatedRowSpotsCount[row]++;
                    consecutivePopulatedRowSpotsCount[row]++;
                    remainingColumnSpots[column]--;

                    isIndividualTicketColumnPopulated = true;
                } else {
                    consecutivePopulatedRowSpotsCount[row] = 0;
                }

                if (row % 3 == 2) {
                    isIndividualTicketColumnPopulated = false;
                }
            }
        }

        consecutivePopulatedRowSpotsCount = new int[18];
        int[] consecutiveNullRowSpotsCount = new int[18];

        for (int column = 0; column < columns; column++) {
            int blankSpotsCount = 12;
            for (int row = 0; row < ticketSheet.length; row++) {
                if (ticketSheet[row][column] == 0) {
                    if (remainingColumnSpots[column] > 0 && totalPopulatedRowSpotsCount[row] < 5
                            && (remainingColumnSpots[column] == blankSpotsCount
                                    || (consecutivePopulatedRowSpotsCount[row] < 2 && random.nextInt(2) == 1)
                                    || consecutiveNullRowSpotsCount[row] == maxConsecutiveNullRowSpots[row])) {
                        ticketSheet[row][column] = 1;
                        totalPopulatedRowSpotsCount[row]++;
                        remainingColumnSpots[column]--;
                        consecutivePopulatedRowSpotsCount[row]++;

                        if (consecutiveNullRowSpotsCount[row] == maxConsecutiveNullRowSpots[row]) {
                            maxConsecutiveNullRowSpots[row] = 1;
                        }

                        consecutiveNullRowSpotsCount[row] = 0;
                    } else {
                        consecutivePopulatedRowSpotsCount[row] = 0;
                        consecutiveNullRowSpotsCount[row]++;
                    }

                    blankSpotsCount--;
                } else {
                    consecutiveNullRowSpotsCount[row] = 0;
                    consecutivePopulatedRowSpotsCount[row]++;
                }
            }
        }
    }

    private static void populateTicketSheet() {
        PlayerNumberTable numberTable = new PlayerNumberTable();
        for (int i = 0; i < columns; i++) {
            int spotsRemaining = totalColumnSpots[i];
            for (int j = 0; j < 6; j++) {
                int prevIndex = 0;
                for (int k = 0; k < rows; k++) {
                    int row = (j * rows) + k;
                    if (ticketSheet[row][i] == 1) {
                        int[] numberResult = numberTable.getNumber(k, i, prevIndex, --spotsRemaining);
                        ticketSheet[row][i] = numberResult[0];
                        prevIndex = numberResult[1];
                    }

                }
            }
        }
    }

    private static TicketSet distributeTickets(int numTickets) {
        ArrayList<Ticket> playerTickets = new ArrayList<Ticket>();

        TicketSet ticketSet = new TicketSet();

        if (numTickets < 1) {
            return ticketSet;
        }

        int ticketNumber = 0;
        Ticket ticket = new Ticket(ticketNumber++);
        int currTicketRow = 0;
        for (int row = 0; row < ticketSheet.length; row++) {
            for (int column = 0; column < columns; column++) {
                ticket.setSpotValue(currTicketRow, column, ticketSheet[row][column]);
            }

            currTicketRow++;

            if (currTicketRow % 3 == 0) {
                playerTickets.add(ticket);
                ticket.printTicket();
                System.out.println();
                ticket = new Ticket(ticketNumber++);
                currTicketRow = 0;
            }

            if (playerTickets.size() == numTickets) {
                ticketSet.setTickets(playerTickets);
                return ticketSet;
            }
        }

        return ticketSet;
    }

    private static void play(ArrayList<Ticket> playerTickets) {
        ArrayList<Integer> numbers = new ArrayList<Integer>();

        for (int i = 1; i <= 90; i++) {
            numbers.add(i);
        }

        try {
            while (!numbers.isEmpty()) {
                int index = random.nextInt(numbers.size());
                int number = numbers.get(index);
                numbers.remove(index);

                // System.out.println("The number is: " + number);
                markNumber(playerTickets, number);

                // System.out.println();

                checkWinner(playerTickets);

                Thread.sleep(50);
            }
        } catch (InterruptedException e) {

        }

    }

    private static void markNumber(ArrayList<Ticket> playerTickets, int number) {
        for (int ticketIndex = 0; ticketIndex < playerTickets.size(); ticketIndex++) {
            Ticket ticket = playerTickets.get(ticketIndex);

            for (int row = 0; row < ticket.getRowSize(); row++) {
                for (int column = 0; column < ticket.getColumnSize(); column++) {
                    if (ticket.markSpot(row, column, number)) {
                        System.out.println(number + " FOUND!");

                        return;
                    }
                }
            }
        }

        System.out.println(number + " not found.");
    }

    private static void checkWinner(ArrayList<Ticket> playerTickets) {
        for (int ticketIndex = 0; ticketIndex < playerTickets.size(); ticketIndex++) {
            Ticket ticket = playerTickets.get(ticketIndex);

            if (ticket.isEarlyFive()) {
                System.out.println("CONGRATS! TICKET " + ticket.getTicketNumber() + " GOT EARLY FIVE!");
            }

            if (ticket.isTopLine()) {
                System.out.println("CONGRATS! TICKET " + ticket.getTicketNumber() + " GOT TOP LINE!");
            }

            if (ticket.isMiddleLine()) {
                System.out.println("CONGRATS! TICKET " + ticket.getTicketNumber() + " GOT MIDDLE LINE!");
            }

            if (ticket.isBottomLine()) {
                System.out.println("CONGRATS! TICKET " + ticket.getTicketNumber() + " GOT BOTTOM LINE!");
            }

            if (ticket.isFullHouse()) {
                System.out.println("CONGRATS! TICKET " + ticket.getTicketNumber() + " GOT FULL HOUSE!");
            }
        }
    }

    private static boolean isValidTicketsLayout() {
        for (int column = 0; column < columns; column++) {
            int spotCount = 0;

            for (int row = 0; row < ticketSheet.length; row++) {
                spotCount += ticketSheet[row][column];
            }

            if (spotCount != totalColumnSpots[column]) {
                return false;
            }
        }

        for (int row = 0; row < ticketSheet.length; row++) {
            int spotCount = 0;

            for (int column = 0; column < columns; column++) {
                spotCount += ticketSheet[row][column];
            }

            if (spotCount != 5) {
                return false;
            }
        }

        return true;
    }

    private static void printTicket() {
        for (int i = 0; i < ticketSheet.length; i++) {
            for (int j = 0; j < columns; j++) {
                System.out.print(ticketSheet[i][j] + " ");
            }
            System.out.println();

            if (i % rows == 2) {
                System.out.println();
            }
        }
    }
}