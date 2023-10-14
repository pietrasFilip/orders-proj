package com.app.persistence.model.customer;

import com.app.persistence.data.reader.model.CustomerData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Customer {
    Long id;
    String name;
    String surname;
    int age;
    String email;

    // Methods that give information about client

    /**
     *
     * @param referenceAge Age given by user.
     * @return True when customer age is lower than reference age or false when not.
     */
    public boolean hasDiscountAge(int referenceAge) {
        return this.age <= referenceAge;
    }

    /**
     *
     * @param email Email adres given by user.
     * @return True when email given by user is the same as customer email or false when not.
     */
    public boolean hasEmail(String email) {
        return Objects.equals(this.email, email);
    }

    // Methods from Object class

    @Override
    public String toString() {
        return "CUSTOMER(name=%s, surname=%s, age=%s, email=%s)".formatted(name, surname, age, email);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer customer)) return false;
        return age == customer.age && Objects.equals(name, customer.name) && Objects.equals(surname, customer.surname) && Objects.equals(email, customer.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, surname, age, email);
    }

    // Convert methods

    public Map.Entry<String, Customer> toEntry() {
        return Map.entry(this.email, this);
    }

    public CustomerData toCustomerData() {
        return CustomerData.of(id, this.name, this.surname, this.age, this.email);
    }

    // Static methods

    public static Customer of(Long id, String name, String surname, int age, String email) {
        return new Customer(id, name, surname, age, email);
    }
}
