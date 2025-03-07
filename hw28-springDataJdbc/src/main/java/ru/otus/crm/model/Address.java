package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("address")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    private Long id;

    private String street;
}
