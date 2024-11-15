package homework;

import java.util.LinkedHashSet;

public class CustomerReverseOrder {
    LinkedHashSet<Customer> customerLinkedHashSet = new LinkedHashSet<>();

    // todo: 2. надо реализовать методы этого класса
    // надо подобрать подходящую структуру данных, тогда решение будет в "две строчки"

    public void add(Customer customer) {
        customerLinkedHashSet.add(customer);
    }

    public Customer take() {
        return customerLinkedHashSet.removeLast();
    }
}
