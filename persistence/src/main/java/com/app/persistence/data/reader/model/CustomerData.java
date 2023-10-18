package com.app.persistence.data.reader.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class CustomerData {
    private Long customerId;
    private String name;
    private String surname;
    private int age;
    private String email;

    public static CustomerData of(Long id, String name, String surname, int age, String email) {
        return new CustomerData(id, name, surname, age, email);
    }
}
