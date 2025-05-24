package br.com.eduardo.novalumeorderservice.repository;

import br.com.eduardo.novalumeorderservice.model.Order;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Profile("test")
class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    private Order sampleOrder;

    @BeforeEach
    void setUp() {
        orderRepository.deleteAll();
        sampleOrder = createSampleOrder();
    }

    @Test
    void shouldSaveOrderSuccessfully() {
        Order savedOrder = orderRepository.save(sampleOrder);

        assertThat(savedOrder).isNotNull();
    }

    @Test
    void shouldReturnEmptyWhenOrderNotFound() {
        Optional<Order> notFound = orderRepository.findById(UUID.randomUUID());

        assertThat(notFound).isEmpty();
    }

    @Test
    void shouldReturnEmptyListWhenNoOrdersForCustomer() {
        UUID nonExistentCustomerId = UUID.randomUUID();

        List<Order> orders = orderRepository.findAllByCustomerId(nonExistentCustomerId);

        assertThat(orders).isEmpty();
    }

    private Order createSampleOrder() {
        Order order = new Order();
        order.setCustomerId(UUID.randomUUID());
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setPaymentId(UUID.randomUUID());
        return order;
    }
}