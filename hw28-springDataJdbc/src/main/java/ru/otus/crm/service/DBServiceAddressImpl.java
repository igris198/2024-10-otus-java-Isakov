package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Address;
import ru.otus.crm.repository.AddressRepository;
import ru.otus.sessionmanager.TransactionManager;

@Service
public class DBServiceAddressImpl implements DBServiceAddress {
    private static final Logger log = LoggerFactory.getLogger(DBServiceAddressImpl.class);

    private final TransactionManager transactionManager;
    private final AddressRepository addressRepository;

    public DBServiceAddressImpl(TransactionManager transactionManager, AddressRepository addressRepository) {
        this.transactionManager = transactionManager;
        this.addressRepository = addressRepository;
    }

    @Override
    public Address saveAddress(Address address) {
        return transactionManager.doInTransaction(() -> {
            var savedAddress = addressRepository.save(address);
            log.info("saved address: {}", savedAddress);
            return savedAddress;
        });
    }
}
