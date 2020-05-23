package com.solace.battleship.models;

import java.util.ArrayList;
import java.util.Random;

public class PlayerNumberTable {
    private ArrayList<ArrayList<Integer>> table;
    private Random random;

    public PlayerNumberTable() {
        table = new ArrayList<ArrayList<Integer>>();
        setupTable();

        random = new Random();
    }

    private void setupTable() {
        for (int i = 0; i < 9; i++) {
            ArrayList<Integer> valueSet = new ArrayList<Integer>();
            table.add(valueSet);

            for (int j = 0; j < 10; j++) {
                if (i != 0 || j != 0) {
                    valueSet.add(j + (10 * i));
                }
            }

            if (i == 8) {
                valueSet.add(90);
            }
        }
    }

    public int[] getNumber(int rowNumber, int columnNumber, int prevIndex, int spotsRemaining) {
        int offset = Math.min(2 - rowNumber, spotsRemaining);
        int index = prevIndex + random.nextInt(table.get(columnNumber).size() - offset - prevIndex);

        int value = table.get(columnNumber).get(index);
        table.get(columnNumber).remove(index);

        int[] result = { value, index };
        return result;
    }
}