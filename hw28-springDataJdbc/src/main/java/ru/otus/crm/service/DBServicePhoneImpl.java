package ru.otus.crm.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.otus.crm.model.Phone;
import ru.otus.crm.repository.PhoneRepository;
import ru.otus.sessionmanager.TransactionManager;

@Service
public class DBServicePhoneImpl implements DBServicePhone {
    private static final Logger log = LoggerFactory.getLogger(DBServicePhoneImpl.class);

    private final TransactionManager transactionManager;
    private final PhoneRepository phoneRepository;

    public DBServicePhoneImpl(TransactionManager transactionManager, PhoneRepository phoneRepository) {
        this.transactionManager = transactionManager;
        this.phoneRepository = phoneRepository;
    }

    @Override
    public Phone savePhone(Phone phone) {
        return transactionManager.doInTransaction(() -> {
            var savedPhone = phoneRepository.save(phone);

            log.info("saved phone: {}", savedPhone);
            return savedPhone;
        });
    }
}
