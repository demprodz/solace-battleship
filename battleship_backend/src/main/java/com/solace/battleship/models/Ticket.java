package com.solace.battleship.models;

public class Ticket {
	private int ticketNumber;
	private Spot[][] ticketMatrix;
	private int populatedSpots;
	private boolean isEliminated;
	private final int EARLY_FIVE = 5;
	private final int MAX_POPULATED_SPOTS = 15;
	private boolean foundEarlyFive;
	private boolean foundTopLine;
	private boolean foundMiddleLine;
	private boolean foundBottomLine;
	private boolean foundFullHouse;

	public Ticket(int ticketNumber) {
		this.ticketNumber = ticketNumber;
		this.ticketMatrix = new Spot[3][9];
		this.isEliminated = false;
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

	public boolean isEarlyFive() {
		// if (markedSpots == EARLY_FIVE && !foundEarlyFive) {
		// foundEarlyFive = true;
		// return true;
		// }

		return false;
	}

	public boolean isTopLine() {
		// if (foundTopLine) {
		// return false;
		// }

		// int sum = 0;
		// for (int column = 0; column < 9; column++) {
		// sum += ticketMatrix[0][column];
		// }

		// if (sum == -5) {
		// foundTopLine = true;
		// return true;
		// }

		return false;
	}

	public boolean isMiddleLine() {
		// if (foundMiddleLine) {
		// return false;
		// }

		// int sum = 0;
		// for (int column = 0; column < 9; column++) {
		// sum += ticketMatrix[1][column];
		// }

		// if (sum == -5) {
		// foundMiddleLine = true;
		// return true;
		// }

		return false;
	}

	public boolean isBottomLine() {
		// if (foundBottomLine) {
		// return false;
		// }

		// int sum = 0;
		// for (int column = 0; column < 9; column++) {
		// sum += ticketMatrix[2][column];
		// }

		// if (sum == -5) {
		// foundBottomLine = true;
		// return true;
		// }

		return false;
	}

	public boolean isFullHouse() {
		// if (markedSpots == MAX_POPULATED_SPOTS && !foundFullHouse) {
		// foundFullHouse = true;
		// return true;
		// }
		return false;
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

	public void printTicket() {
		for (int i = 0; i < ticketMatrix.length; i++) {
			for (int j = 0; j < ticketMatrix[0].length; j++) {
				System.out.print(ticketMatrix[i][j] + " ");
			}
			System.out.println();
		}
	}
}