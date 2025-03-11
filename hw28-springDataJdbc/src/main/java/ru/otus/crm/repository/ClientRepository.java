package ru.otus.crm.repository;

import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.ListCrudRepository;
import ru.otus.crm.model.Client;

public interface ClientRepository extends ListCrudRepository<Client, Long> {
    @Override
    @Query(
            value =
                    """
                            SELECT
                             c.id client_id,
                             c.name client_name,
                             c.address_id,
                             a.street,
                             p.id phone_id,
                             p.number phone_number
                            FROM client c
                            LEFT JOIN address a ON a.id = c.address_id
                            LEFT JOIN phones p ON p.client_id = c.id
                            """,
            resultSetExtractorClass = ClientResultSetExtractorClass.class)
    List<Client> findAll();
}
