package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;

public class CashBox {
    private static final Logger logger = LoggerFactory.getLogger(CashBox.class);
    private static final Comparator<CashBox> comparator = (cashBox1, cashBox2) -> cashBox2.packOfBanknotes.getBanknote().getValue() - cashBox1.packOfBanknotes.getBanknote().getValue();

    private final PackOfBanknotes packOfBanknotes;

    public CashBox(Banknote banknote, int amount) {
        this.packOfBanknotes = new PackOfBanknotes(banknote, amount);
    }

    public static Comparator<CashBox> getComparator(){
        return comparator;
    }

    public boolean isEmpty() {
        return packOfBanknotes.isEmpty();
    }

    public int getAvailableSum(int requestedSum) {
        if ( isEmpty() || packOfBanknotes.getBanknote().getValue() > requestedSum ) return 0;

        int requestedCnt = requestedSum / packOfBanknotes.getBanknote().getValue();
        return Math.min(requestedCnt, packOfBanknotes.getAmount()) * packOfBanknotes.getBanknote().getValue();
    }

    public PackOfBanknotes getPackOfBanknotes(int sum) {
        int availableSum = getAvailableSum(sum);
        if (availableSum == 0) return null;

        int packAmount = availableSum / packOfBanknotes.getBanknote().getValue();
        packOfBanknotes.setAmount(packOfBanknotes.getAmount() - packAmount);
        return new PackOfBanknotes(packOfBanknotes.getBanknote(), packAmount);
    }

    public int getBalance(){
        logger.info("В наличии купюры по {} руб.: {} шт.", packOfBanknotes.getBanknote().getValue(), packOfBanknotes.getAmount());
        return getAvailableSum(Integer.MAX_VALUE);
    }
}
