package com.solace.battleship.models;

import java.util.Random;
import com.solace.battleship.events.NextNumberChooseResult;

public class GameNumberSet {
    private HousieNumber[][] numberSet;
    private IPrize[] prizes;
    private Random random;
    private int numbersLeft;

    public GameNumberSet() {
        random = new Random();
        numbersLeft = 0;
        initialize();
    }

    public HousieNumber[][] getNumberSet() {
        return numberSet;
    }

    public int getNumbersLeft() {
        return numbersLeft;
    }

    public NextNumberChooseResult chooseNextNumber() {
        int rowIndex = random.nextInt(numberSet.length);
        int columnIndex = random.nextInt(numberSet[0].length);
        HousieNumber number = numberSet[rowIndex][columnIndex];

        while (number.getIsMarked()) {
            rowIndex = random.nextInt(numberSet.length);
            columnIndex = random.nextInt(numberSet[0].length);
            number = numberSet[rowIndex][columnIndex];
        }

        number.setIsMarked(true);
        numbersLeft--;

        return new NextNumberChooseResult(number.getValue(), rowIndex, columnIndex, numbersLeft == 0);
    }

    public boolean isNumberMarked(int value) {
        int columnIndex = (value - 1) % 10;
        int rowIndex = value / 10;

        return numberSet[rowIndex][columnIndex].getIsMarked();
    }

    public IPrize[] getPrizes() {
        return prizes;
    }

    public boolean isAllPrizesTaken() {
        for (int i = 0; i < prizes.length; i++) {
            if (!prizes[i].getIsTaken()) {
                return false;
            }
        }

        return true;
    }

    private void initialize() {
        numberSet = new HousieNumber[9][10];

        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 10; j++) {
                numberSet[i][j] = new HousieNumber((i * 10) + j + 1);
                numbersLeft++;
            }
        }

        prizes = new IPrize[] { new EarlyFivePrize(1), new TopLinePrize(1), new MiddleLinePrize(1),
                new BottomLinePrize(1), new FourCornersPrize(1), new FullHousePrize(3) };
    }
}