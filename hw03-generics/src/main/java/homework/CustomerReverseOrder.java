package homework;

import java.util.*;

public class CustomerReverseOrder {
    private final Deque<Customer> customerDeque = new ArrayDeque<>();

    // todo: 2. надо реализовать методы этого класса
    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        customerDeque.addLast(customer);
    }

    public Customer take() {
        return customerDeque.pollLast();
    }
}
