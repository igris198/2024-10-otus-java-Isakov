package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        ATM atm = new ATM(4);
        try {
            atm.addCashBox(new CashBox(Banknote.FIVE_THOUSAND, 1));
            atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
            atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
            atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));
            atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));
        } catch (Exception e) {
            logger.error("Превышено максимальное число заполненных ячеек", e);
        }
        atm.getCashBalance();

        FullPackOfBanknotes fullPackOfBanknotes;

        fullPackOfBanknotes = atm.getCash(500);
        if (fullPackOfBanknotes != null) fullPackOfBanknotes.printSum();

        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        fullPackOfBanknotes = atm.getCash(1500);
        if (fullPackOfBanknotes != null) fullPackOfBanknotes.printSum();

        fullPackOfBanknotes =  atm.getCash(600);
        if (fullPackOfBanknotes != null) fullPackOfBanknotes.printSum();

        atm.getCashBalance();

        atm.addCash(Banknote.FIFTY, 4);
        atm.addCash(Banknote.ONE_HUNDRED, 2);

        atm.getCashBalance();

        fullPackOfBanknotes =  atm.getCash(600);
        if (fullPackOfBanknotes != null) fullPackOfBanknotes.printSum();

        atm.getCashBalance();
    }
}