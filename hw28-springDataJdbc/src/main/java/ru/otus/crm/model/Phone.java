package ru.otus.crm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("phones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Phone {
    @Id
    private Long id;

    private String number;

    private Long clientId;
}
