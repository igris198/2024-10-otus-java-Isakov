import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.ATM;
import ru.otus.Banknote;
import ru.otus.CashBox;
import ru.otus.FullPackOfBanknotes;

public class ATMTest {
    @Test
    @DisplayName("Новый ATM пуст")
    void emptyATMTest(){
        ATM atm = new ATM(4);

        assertThat(atm.getCashBalance()).isZero();
    }

    @Test
    @DisplayName("В ATM добавляются кассеты с купюрами")
    void addCashToATMTest(){
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.FIVE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        assertThat(atm.getCashBalance()).isEqualTo(7500);
    }

    @Test
    @DisplayName("Попытка добавить лишнюю кассету в ATM вызывает ошибку")
    void exceptionAddCashToATMTest(){
        ATM atm = new ATM(2);
        atm.addCashBox(new CashBox(Banknote.FIVE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));

        Exception exception = assertThrows(RuntimeException.class,() -> atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1)));
        assertEquals("Превышено максимальное число заполненных ячеек",exception.getMessage());
    }

    @Test
    @DisplayName("ATM выдает деньги")
    void getCashTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.FIVE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        FullPackOfBanknotes fullPackOfBanknotes;

        fullPackOfBanknotes = atm.getCash(1500);
        fullPackOfBanknotes.printSum();
        assertThat(fullPackOfBanknotes.getSum()).isEqualTo(1500);
    }

    @Test
    @DisplayName("ATM не выдает сумму меньше минимальной")
    void getLessThanMinCashTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.FIVE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        FullPackOfBanknotes fullPackOfBanknotes;

        fullPackOfBanknotes = atm.getCash(50);
        assertThat(fullPackOfBanknotes).isNull();
    }

    @Test
    @DisplayName("ATM не выдает сумму больше максимальной")
    void getGreaterThanMaxCashTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        FullPackOfBanknotes fullPackOfBanknotes;
        fullPackOfBanknotes = atm.getCash(2500);
        fullPackOfBanknotes.printSum();

        fullPackOfBanknotes = atm.getCash(50);
        assertThat(fullPackOfBanknotes).isNull();
    }

    @Test
    @DisplayName("ATM не выдает сумму которую невозможно сложить из имеющихся купюр")
    void getNotExistingBillsTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        FullPackOfBanknotes fullPackOfBanknotes;
        fullPackOfBanknotes = atm.getCash(650);
        assertThat(fullPackOfBanknotes).isNull();
    }

    @Test
    @DisplayName("Внесение купюр в имеющиеся ячейки")
    void addBanknotesTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));

        atm.addCash(Banknote.ONE_THOUSAND,1);
        assertThat(atm.getCashBalance()).isEqualTo(3000);
    }

    @Test
    @DisplayName("ATM не принимает купюры номинала которых нет в имеющихся ячейках")
    void addWrongDenominationTest() {
        ATM atm = new ATM(4);
        atm.addCashBox(new CashBox(Banknote.ONE_THOUSAND, 1));
        atm.addCashBox(new CashBox(Banknote.ONE_HUNDRED, 10));

        atm.addCash(Banknote.FIVE_HUNDRED,1);

        assertThat(atm.getCashBalance()).isEqualTo(2000);
    }

    @Test
    @DisplayName("В ATM добавляется новая кассета взамен пустой")
    void addReplaceBoxTest() {
        ATM atm = new ATM(1);
        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        FullPackOfBanknotes fullPackOfBanknotes = atm.getCash(500);
        fullPackOfBanknotes.printSum();
        assertThat(fullPackOfBanknotes.getSum()).isEqualTo(500);

        atm.addCashBox(new CashBox(Banknote.FIVE_HUNDRED, 1));

        assertThat(atm.getCashBalance()).isEqualTo(500);
    }

}
