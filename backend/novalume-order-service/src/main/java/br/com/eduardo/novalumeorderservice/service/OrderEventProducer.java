package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.CreatePaymentEventDto;
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

    public void sendMessage(CreatePaymentEventDto createPaymentEventDto) {
        try {
            rabbitTemplate.convertAndSend(TOPIC, createPaymentEventDto);
            log.info("Message was sent with success: id: {}, queue: {}", createPaymentEventDto.orderId(), TOPIC);
        } catch (AmqpException e) {
            log.error("Error to send to queue '{}': {}", TOPIC, e.getMessage(), e);
        }
    }
}
