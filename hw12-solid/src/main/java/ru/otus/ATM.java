package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

public class ATM {
    private static final Logger logger = LoggerFactory.getLogger(ATM.class);

    private TreeSet<CashBox> cashBoxes;
    private final int cashBoxesMaxValue;

    public ATM(int cashBoxesMaxValue) {
        this.cashBoxes = new TreeSet<>(CashBox.getComparator());
        this.cashBoxesMaxValue = cashBoxesMaxValue;
    }

    public void addCashBox(CashBox cashBox) {
        cashBoxes = deleteEmptyCashBox(cashBox.getBanknoteValue());
        if (cashBoxes.size() < cashBoxesMaxValue) {
            cashBoxes.add(cashBox);
            logger.info("Добавлена ячейка с купюрами {} руб.", cashBox.getBanknoteValue());
        }
        else { throw new RuntimeException("Превышено максимальное число заполненных ячеек"); }
    }

    public TreeSet<CashBox> deleteEmptyCashBox(int value) {
        return cashBoxes.stream().filter(cashBox -> !(cashBox.isEmpty() & cashBox.getBanknoteValue() == value)).collect(Collectors.toCollection(() -> new TreeSet<>(CashBox.getComparator())));
    }

    public boolean checkSum(int sum) {
        int remainingSum = sum;
        for (CashBox cashBox : cashBoxes) {
            remainingSum -= cashBox.getAvailableSum(remainingSum);
            if (remainingSum == 0) return true;
        }
        return false;
    }

    public FullPackOfBanknotes getCash(int sum) {
        logger.info("Запрошена сумма к выдаче: {} руб.", sum);

        if (!checkSum(sum)) {
            logger.error("Невозможно выдать требуемую сумму");
            return null;
        }

        FullPackOfBanknotes fullPackOfBanknotes = new FullPackOfBanknotes();
        int remainingSum = sum;
        for (CashBox cashBox : cashBoxes) {
            PackOfBanknotes packOfBanknotes = cashBox.getPackOfBanknotes(remainingSum);
            if (packOfBanknotes != null) {
                fullPackOfBanknotes.addPackOfBanknotes(packOfBanknotes);
                remainingSum -= packOfBanknotes.getSum();
            }
            if (remainingSum == 0) break;
        }
        return fullPackOfBanknotes;
    }

    public void addCash(Banknote banknote, int amount){
        for (CashBox cashBox : cashBoxes) {
            if (cashBox.getBanknoteValue() == banknote.getValue()) {
                cashBox.addBanknotes(amount);
                logger.info("Добавлены купюры в {} руб. количеством {} шт", banknote.getValue(), amount);
                return;
            }
        }
        logger.error("Невозможно добавить банкноты номинала {} руб.", banknote.getValue());
    }

    public int getCashBalance(){
        int balance = cashBoxes.stream().reduce(0, (sum, cashBox) -> sum + cashBox.getBalance(), Integer::sum);

        if (balance == 0) { logger.info("Денежные средства в банкомате отсутствуют."); }
        else { logger.info("Общий остаток денежных средств: {} руб.", balance); }

        return balance;
    }
}
