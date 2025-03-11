package ru.otus.crm.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {
    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        Long prevClientId = null;
        Client client = null;
        while (rs.next()) {
            var clientId = rs.getLong("client_id");
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(clientId, rs.getString("client_name"), rs.getLong("address_id"), new ArrayList<>());
                client.setAddress(new Address(rs.getLong("address_id"), rs.getString("street")));
                clientList.add(client);
                prevClientId = clientId;
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (phoneId != null) {
                client.getPhones().add(new Phone(phoneId, rs.getString("phone_number"), clientId));
            }
        }
        return clientList;
    }
}
