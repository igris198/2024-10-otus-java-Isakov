package homework;

import java.util.*;

public class CustomerService {
    private final NavigableMap<Customer, String> customerStringTreeMap = new TreeMap<>(Comparator.comparingLong(Customer::getScores));

    private Map.Entry<Customer, String> getEntryClone(Customer customer) {
        if (customer == null) return null;
        Customer newCustomer = new Customer(customer.getId(), customer.getName(), customer.getScores());
        return new AbstractMap.SimpleImmutableEntry<>(newCustomer, customerStringTreeMap.get(newCustomer));
    }

    public Map.Entry<Customer, String> getSmallest() {
        return getEntryClone(customerStringTreeMap.firstKey());
    }

    public Map.Entry<Customer, String> getNext(Customer customer) {
        return getEntryClone(customerStringTreeMap.higherKey(customer));
    }

    public void add(Customer customer, String data) {
        customerStringTreeMap.put(customer, data);
    }
}
