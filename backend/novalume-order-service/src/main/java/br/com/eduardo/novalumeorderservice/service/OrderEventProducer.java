package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.OrderMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProducer {
    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.producer.topic}")
    private String TOPIC;

    public void sendMessage(OrderMessage orderMessage) {
        try {
            rabbitTemplate.convertAndSend(TOPIC, orderMessage);
            log.info("Message was sent with success: id: {}, queue: {}", orderMessage.orderId(), TOPIC);
        } catch (AmqpException e) {
            log.error("Error to send to queue '{}': {}", TOPIC, e.getMessage(), e);
        }
    }
}
