package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class CashBox {
    private static final Logger logger = LoggerFactory.getLogger(CashBox.class);
    private static final Comparator<CashBox> comparator = (cashBox1, cashBox2) -> cashBox2.getBanknoteValue() - cashBox1.getBanknoteValue();

    private final Banknote banknote;
    private int amount;

    public CashBox(Banknote banknote, int amount) {
        this.banknote = banknote;
        this.amount = amount;
    }

    public int getBanknoteValue() {
        return banknote.getValue();
    }

    public void addBanknotes(int amount) {
        this.amount += amount;
    }

    private boolean withdrawBanknotes(int amount) {
        if (amount > this.amount) { return false; }
        this.amount -= amount;
        return true;
    }

    public static Comparator<CashBox> getComparator(){
        return comparator;
    }

    public boolean isEmpty() {
        return amount == 0;
    }

    public int getAvailableSum(int requestedSum) {
        if ( isEmpty() || banknote.getValue() > requestedSum ) return 0;

        int requestedCnt = requestedSum / getBanknoteValue();
        return Math.min(requestedCnt, amount) * getBanknoteValue();
    }

    public PackOfBanknotes getPackOfBanknotes(int sum) {
        int availableSum = getAvailableSum(sum);
        if (availableSum == 0) return null;

        int packAmount = availableSum / banknote.getValue();

        if (!withdrawBanknotes(packAmount)) {
            String messageText = "Ошибка снятия банкнот " + getBanknoteValue() + " руб.";
            logger.error(messageText);
            throw new RuntimeException(messageText);
        }

        return new PackOfBanknotes(banknote, packAmount);
    }

    public int getBalance(){
        logger.info("В наличии купюры по {} руб.: {} шт.", getBanknoteValue(), amount);
        return getAvailableSum(Integer.MAX_VALUE);
    }
}
