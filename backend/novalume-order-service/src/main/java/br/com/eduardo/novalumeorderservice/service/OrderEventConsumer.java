package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.OrderPaymentEvent;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import br.com.eduardo.novalumeorderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderEventConsumer {
    private final OrderRepository orderRepository;

    @RabbitListener(queues = "${spring.rabbitmq.consumer.topic}")
    public void ProcessOrderStatus(OrderPaymentEvent orderPaymentEvent) {
        orderRepository.findById(orderPaymentEvent.orderId())
                .filter(order -> order.getStatus() == OrderStatus.PENDING)
                .ifPresent(order -> {
                    switch (orderPaymentEvent.paymentType()) {
                        case PAYMENT_CONFIRMED -> order.setStatus(OrderStatus.PROCESSING);
                        case PAYMENT_FAILED -> order.setStatus(OrderStatus.CANCELLED);
                    }
                    orderRepository.save(order);
                });
    }
}
