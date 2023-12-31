package com.spincity.roulette.bet;

import com.spincity.roulette.spinner.SpinnerNumber;

public class ColorBet extends BetCalculator {

    public ColorBet(Bet bet) {
        super(bet);
    }

    @Override
    public double calculateWinLoss(SpinnerNumber spinnerNumber) {
        // Do some calculation

        if (getBet().getOption().equals(spinnerNumber.color())) {
            return getBet().getChip().value() * getBet().getType().multiplier();
        }

        return 0.0;
    }

}
