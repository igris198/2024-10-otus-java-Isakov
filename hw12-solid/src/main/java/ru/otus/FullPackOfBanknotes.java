package ru.otus;

import java.util.*;

public class FullPackOfBanknotes {
    private final List<PackOfBanknotes> packOfBanknotesList;

    public FullPackOfBanknotes() {
        this.packOfBanknotesList = new ArrayList<>();
    }

    public void addPackOfBanknotes(PackOfBanknotes packOfBanknotes){
        packOfBanknotesList.add(packOfBanknotes);
    }

    public int getSum() {
        return packOfBanknotesList.stream().mapToInt(PackOfBanknotes::getSum).sum();
    }

    public void printSum() {
        packOfBanknotesList.forEach(PackOfBanknotes::printSum);
    }
}
