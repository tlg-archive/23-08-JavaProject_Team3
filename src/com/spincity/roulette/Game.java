package com.spincity.roulette;

import static com.spincity.roulette.utils.ANSI.*;
import static com.spincity.roulette.utils.ErrorMessages.errorMessageInvalidEntry;

import com.apps.util.Console;
import com.apps.util.Prompter;
import com.spincity.roulette.bet.*;
import com.spincity.roulette.account.Account;
import com.spincity.roulette.account.Player;
import com.spincity.roulette.bet.selection.BetSelection;
import com.spincity.roulette.spinner.RouletteWheel;
import com.spincity.roulette.spinner.SpinnerNumber;
import com.spincity.roulette.utils.Sleep;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Game {
    private final Board board;
    private SpinnerNumber winningNumber;
    private final List<Bet> bets;
    private Player player;
    private final Prompter prompter = new Prompter(new Scanner(System.in));

    public static void main(String[] args) throws InterruptedException {
        // TODO: Remove in final version
        Account account = Account.createNewAccount("Kobi");
        Player testPlayer = account.getPlayer();
        Game game = new Game(testPlayer);
        game.play();
    }

    public Game(Player player) {
        setPlayer(player);
        board = new Board();
        bets = new ArrayList<>();
    }

    public void play() throws InterruptedException {
        promptUserToSelectBet();
        displaySpinner();
        showWonOrLostScreen();
        displayPlayAgainPrompt();
    }

    private void displayPlayAgainPrompt() {
        /*
         * Ask if player want to play another game if player still has some balance in account.
         * TODO: This logic could potentially be in the main controller.
         */
        if (player.getAccountBalance() > 0) {
            String continuePlayInput = prompter.prompt("Would you to play again (Y/N)? ", "[YyNn]", errorMessageInvalidEntry());
            player.setWantsToPlay(continuePlayInput.equals("Y") || continuePlayInput.equals("y"));
        } else {
            prompter.prompt("Insufficient funds to play another game. Press any key to exit the game!");
            player.setWantsToPlay(false);
        }
    }

    private void gameStats() {
        String accountBalanceText = String.format("Account Balance: $%,.2f", player.getAccountBalance());
        System.out.printf(" Player Name: %-55s %30s\n", player.getName(), accountBalanceText);
    }

    private void showWonOrLostScreen() {
        refreshScreen();
        /*
         * Calculate Winnings
         */
        double amountWon = calculateWinnings(bets, winningNumber);
        player.addAmount(amountWon);

        /*
         * Display Win/Lose Screen
         */
        System.out.println("\nThe winning number is " + colorGreen(String.valueOf(winningNumber.getNumber())) + Color.RESET +
                " & Color is " + winningNumber.color() + "\n");

        SplashScreen splashScreen;
        int frameDelayTimer = 5000;
        Sleep.sleep(frameDelayTimer);

        if (amountWon == 0.0) {
            splashScreen = new SplashScreen("Lost");
            splashScreen.run();
            System.out.println(colorRed("\nSorry! you did not win anything. Better luck next time!\n"));
        } else {
            splashScreen = new SplashScreen("Win");
            splashScreen.run();
            System.out.printf(colorGreen("Congratulations! You won $%,.2f\n"), amountWon);
            System.out.printf("Your new account Balance is: $%,.2f\n", player.getAccountBalance());
            System.out.println();
        }
    }

    private void promptUserToSelectBet() {
        boolean userWantsToPlaceBet = true;
        while (userWantsToPlaceBet) {
            refreshScreen();
            Bet bet = betSelection();
            player.subtractAmount(bet.getChip().value());
            board.placeChips(bet.getOption().boardElement(), bet.getChip());
            bets.add(bet);

            refreshScreen();
            System.out.println("***********************************************");
            System.out.println("**  Increase your odds by adding another bet **");
            System.out.println("***********************************************\n");

            if (player.getAccountBalance() > 0.0) {
                String continueInput = prompter.prompt("Would you like to add another bet (Y/N): ", "[YyNn]", errorMessageInvalidEntry());
                if (continueInput.equals("N") || continueInput.equals("n")) {
                    userWantsToPlaceBet = false;
                }
            } else {
                prompter.prompt(colorRed("Sorry! Insufficient funds to place another bet!\n") + "Press enter to continue with existing bets.");
                userWantsToPlaceBet = false;
            }
        }
    }

    private Bet betSelection() {

        BetSelection betSelection = new BetSelection();
        BetType betType = betSelection.betTypeSelection();

        refreshScreen();
        BetOption betOption = betSelection.betOptionSelection(betType);

        refreshScreen();
        Board.Chip chip = betSelection.selectChip(player);

        return new Bet(betType, betOption, chip);
    }

    public void displaySpinner() {
        RouletteWheel rouletteWheel = new RouletteWheel();
        int pickedNumber = rouletteWheel.run();
        winningNumber = SpinnerNumber.values()[pickedNumber];
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void refreshScreen() {
        Console.clear();
        gameStats();
        board.display();
    }

    private double calculateWinnings(List<Bet> bets, SpinnerNumber winningNumber) {
        double amountWon = 0.0;

        for (Bet bet : bets) {
            BetCalculator betCalculator = BetFactory.bettingStrategy(bet);
            assert betCalculator != null;
            amountWon += betCalculator.calculateWinLoss(winningNumber);
        }

        return amountWon;
    }
}
