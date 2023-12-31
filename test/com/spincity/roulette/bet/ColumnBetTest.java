package com.spincity.roulette.bet;

import com.spincity.roulette.Board;
import com.spincity.roulette.spinner.SpinnerNumber;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class ColumnBetTest {
    Set<Integer> column1Set;
    Set<Integer> column2Set;
    Set<Integer> column3Set;
    Bet bet;

    @Before
    public void init() {
        int[] column1Nums = {1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31, 34};
        column1Set = Arrays.stream(column1Nums).boxed().collect(Collectors.toSet());

        int[] column2Nums = {2, 5, 8, 11, 14, 17, 20, 23, 26, 29, 32, 35};
        column2Set = Arrays.stream(column2Nums).boxed().collect(Collectors.toSet());

        int[] column3Nums = {3, 6, 9, 12, 15, 18, 21, 24, 27, 30, 33, 36};
        column3Set = Arrays.stream(column3Nums).boxed().collect(Collectors.toSet());

        bet = new Bet(BetType.COLUMN, Board.Chip.CHIP_100);
    }

    @Test
    public void columnBetCalculation_1stColumn() {
        bet.setOption(BetType.Column.COLUMN_1_ENDS_34);
        BetCalculator betCalculator = new ColumnBet(bet);

        for (SpinnerNumber spinnerNumber : SpinnerNumber.values()) {
            int winningNumber = spinnerNumber.getNumber();
            double amountWon = betCalculator.calculateWinLoss(spinnerNumber);

            if (column1Set.contains(winningNumber)) {
                assertEquals("SpinnerNumber " + winningNumber + " should return 3x", 300.0, amountWon, 0.001);
            } else {
                assertEquals("SpinnerNumber " + winningNumber + " should return 0x", 0.0, amountWon, 0.001);
            }
        }
    }

    @Test
    public void columnBetCalculation_2ndColumn() {
        bet.setOption(BetType.Column.COLUMN_2_ENDS_35);
        BetCalculator betCalculator = new ColumnBet(bet);

        for (SpinnerNumber spinnerNumber : SpinnerNumber.values()) {
            int winningNumber = spinnerNumber.getNumber();
            double amountWon = betCalculator.calculateWinLoss(spinnerNumber);

            if (column2Set.contains(winningNumber)) {
                assertEquals("SpinnerNumber " + winningNumber + " should return 3x", 300.0, amountWon, 0.001);
            } else {
                assertEquals("SpinnerNumber " + winningNumber + " should return 0x", 0.0, amountWon, 0.001);
            }
        }
    }

    @Test
    public void columnBetCalculation_3rdColumn() {
        bet.setOption(BetType.Column.COLUMN_3_ENDS_36);
        BetCalculator betCalculator = new ColumnBet(bet);

        for (SpinnerNumber spinnerNumber : SpinnerNumber.values()) {
            int winningNumber = spinnerNumber.getNumber();
            double amountWon = betCalculator.calculateWinLoss(spinnerNumber);

            if (column3Set.contains(winningNumber)) {
                assertEquals("SpinnerNumber " + winningNumber + " should return 3x", 300.0, amountWon, 0.001);
            } else {
                assertEquals("SpinnerNumber " + winningNumber + " should return 0x", 0.0, amountWon, 0.001);
            }
        }
    }
}