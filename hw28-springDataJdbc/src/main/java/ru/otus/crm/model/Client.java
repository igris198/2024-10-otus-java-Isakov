package ru.otus.crm.model;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@NoArgsConstructor
@Table("client")
public class Client {
    @Id
    private Long id;

    private String name;

    private Long addressId;

    @Transient
    private Address address;

    @Transient
    private List<Phone> phones;

    public Client(Long id, String name, Long addressId, List<Phone> phones) {
        this.id = id;
        this.name = name;
        this.addressId = addressId;
        this.phones = phones;
    }

    @Override
    public String toString() {
        return "Client{" + "id=" + id + ", name='" + name + '\'' + '}';
    }
}
