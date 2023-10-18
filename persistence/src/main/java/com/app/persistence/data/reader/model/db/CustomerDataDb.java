package com.app.persistence.data.reader.model.db;

import com.app.persistence.data.reader.model.CustomerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerDataDb {
    private Long id;
    private String name;
    private String surname;
    private int age;
    private String email;

    public CustomerData toCustomerData() {
        return CustomerData.of(id, name, surname, age, email);
    }
    public boolean hasDifferentFields(CustomerDataDb customer) {
        return !name.equals(customer.name) || !surname.equals(customer.surname) ||
                age != customer.age;
    }
}
