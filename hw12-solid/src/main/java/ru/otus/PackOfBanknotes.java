package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PackOfBanknotes {
    private static final Logger logger = LoggerFactory.getLogger(PackOfBanknotes.class);

    private final Banknote banknote;
    private int amount;

    public PackOfBanknotes(Banknote banknote, int amount) {
        this.banknote = banknote;
        this.amount = amount;
    }

    public Banknote getBanknote() {
        return banknote;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    public int getSum(){
        return banknote.getValue() * amount;
    }

    public void printSum() {
        logger.info("Выданы купюры по {} руб.: {} шт.", banknote.getValue(), amount);
    }
}
