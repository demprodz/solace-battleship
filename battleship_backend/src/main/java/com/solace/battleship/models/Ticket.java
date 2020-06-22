package com.solace.battleship.models;

import java.util.HashSet;

public class Ticket {
	private int ticketNumber;
	private Spot[][] ticketMatrix;
	private int populatedSpots;
	private boolean isEliminated;
	private HashSet<String> claimedPrizes;
	private final int MAX_POPULATED_SPOTS = 15;

	public Ticket(int ticketNumber) {
		this.ticketNumber = ticketNumber;
		this.ticketMatrix = new Spot[3][9];
		this.isEliminated = false;
		this.claimedPrizes = new HashSet<String>();
	}

	public int getPopulatedSpots() {
		return populatedSpots;
	}

	public Spot[][] getTicketMatrix() {
		return ticketMatrix;
	}

	public boolean setSpotValue(int row, int column, int value) {
		if (row >= 3 || column >= 9 || value == 0 || populatedSpots == MAX_POPULATED_SPOTS) {
			return false;
		}

		ticketMatrix[row][column] = new Spot(value);
		populatedSpots++;

		return true;
	}

	public boolean markSpot(int row, int column) {
		if (row >= 3 || column >= 9) {
			return false;
		}

		return ticketMatrix[row][column].toggleMarked();
	}

	public int getRowSize() {
		return 3;
	}

	public int getColumnSize() {
		return 9;
	}

	public int getTicketNumber() {
		return ticketNumber;
	}

	public void setIsEliminated(boolean isEliminated) {
		this.isEliminated = isEliminated;
	}

	public boolean getIsEliminated() {
		return isEliminated;
	}

	public HashSet<String> getClaimedPrizes() {
		return claimedPrizes;
	}

	public boolean prizeIsClaimed(String prizeName) {
		return claimedPrizes.contains(prizeName);
	}

	public void addClaimedPrize(String prizeName) {
		claimedPrizes.add(prizeName);
	}

	public void printTicket() {
		for (int i = 0; i < ticketMatrix.length; i++) {
			for (int j = 0; j < ticketMatrix[0].length; j++) {
				System.out.print(ticketMatrix[i][j].getValue() + "("
						+ new Boolean(ticketMatrix[i][j].getIsMarked()).toString() + ") ");
			}
			System.out.println();
		}
	}

	public void printMarkedSpots() {
		System.out.println("Marked spots:");

		for (int i = 0; i < ticketMatrix.length; i++) {
			for (int j = 0; j < ticketMatrix[0].length; j++) {
				if (ticketMatrix[i][j] != null && ticketMatrix[i][j].getIsMarked()) {
					System.out.print(ticketMatrix[i][j].getValue() + " ");
				}
			}
			System.out.println();
		}
		System.out.println();
	}
}