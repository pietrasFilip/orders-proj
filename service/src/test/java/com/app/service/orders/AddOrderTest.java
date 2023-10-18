package com.app.service.orders;

import com.app.persistence.data.reader.loader.repository.CustomerRepository;
import com.app.persistence.data.reader.model.db.CustomerDataDb;
import com.app.persistence.model.dto.order.CreateOrderDto;
import com.app.persistence.model.dto.validator.CreateOrderDtoValidator;
import com.app.service.orders.exception.OrdersServiceException;
import com.app.service.orders.provider.OrdersProvider;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@RequiredArgsConstructor
class AddOrderTest {
    @Mock
    CustomerRepository customerRepository;
    @Mock
    CreateOrderDtoValidator createOrderDtoValidator;
    @Mock
    OrdersProvider ordersProvider;
    @InjectMocks
    OrdersServiceImpl ordersService;
    @Test
    @DisplayName("When customer exists and createOrderDto has different data and the same email as existing customer")
    void test1() {
        var createOrderDto = new CreateOrderDto("AA", "A", 25, "a@gmail.com",
                1L, 1);

        when(customerRepository.findByEmail("a@gmail.com"))
                .thenReturn(Optional.of(CustomerDataDb
                        .builder()
                        .name("A")
                        .surname("A")
                        .email("a@gmail.com")
                        .age(25)
                        .build()));

        assertThatThrownBy(() -> ordersService.addOrder(createOrderDto))
                .isInstanceOf(OrdersServiceException.class)
                .hasMessage("Another data is associated with given email");
    }
}
