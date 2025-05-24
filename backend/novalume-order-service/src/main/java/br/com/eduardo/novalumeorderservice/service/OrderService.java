package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderResponseDto;
import br.com.eduardo.novalumeorderservice.dto.orderitem.OrderItemDto;
import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.infra.exception.custom.OrderNotFoundException;
import br.com.eduardo.novalumeorderservice.mapper.OrderMapper;
import br.com.eduardo.novalumeorderservice.model.Order;
import br.com.eduardo.novalumeorderservice.model.OrderItem;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import br.com.eduardo.novalumeorderservice.repository.OrderRepository;
import br.com.eduardo.novalumeorderservice.service.clients.ProductCatalogClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductCatalogClient catalogClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderEventProducer producer;

    private final ObjectMapper objectMapper;

    public void createOrder(OrderCreateDto orderCreateDto) {
        Order newOrder = initializeOrder(orderCreateDto);
        addOrderItems(newOrder, orderCreateDto.items());
        finalizeOrder(newOrder);
    }

    public OrderResponseDto getOderById(UUID orderId, String filters) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new OrderNotFoundException("Order not found"));

        OrderResponseDto orderResponseDto = orderMapper.mapOrderEntityToOrderResponseDto(order);

        if (filters != null && !filters.isEmpty()) {
            return returnFilteredOrder(orderResponseDto, filters);
        }

        return orderResponseDto;
    }

    private Order initializeOrder(OrderCreateDto orderCreateDto) {
        Order newOrder = new Order();
        newOrder.setCustomerId(orderCreateDto.customerId());
        newOrder.setAddressId(orderCreateDto.addressId());
        newOrder.setPaymentId(orderCreateDto.paymentId());
        newOrder.setCustomerName("teste");
        newOrder.setStatus(OrderStatus.PENDING);
        return newOrder;
    }

    private void addOrderItems(Order order, List<OrderItemDto> items) {
        for (OrderItemDto itemDto : items) {
            OrderItem orderItem = createOrderItemFromDto(itemDto);
            order.addItem(orderItem);
        }
    }

    private OrderItem createOrderItemFromDto(OrderItemDto itemDto) {
        ProductDto productItem = catalogClient.getProductById(itemDto.productId());

        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(itemDto.quantity());
        orderItem.setProductId(itemDto.productId());
        orderItem.setProductName(productItem.productName());
        orderItem.setUnitPrice(BigDecimal.valueOf(productItem.unitPrice()));
        orderItem.calculateSubtotal();

        return orderItem;
    }

    protected void finalizeOrder(Order order) {
        order.calculateTotalAmount();
        orderRepository.save(order);
        producer.sendMessage(orderMapper.mapOrderEntityToMessage(order));
    }

    private OrderResponseDto returnFilteredOrder(OrderResponseDto order, String fields) {
        try {
            Set<String> fieldSet = Arrays.stream(fields.split(","))
                    .map(String::trim)
                    .collect(Collectors.toSet());

            Map<String, Object> mappedObject = objectMapper.convertValue(order, Map.class);

            mappedObject.keySet().retainAll(fieldSet);

            return objectMapper.convertValue(mappedObject, OrderResponseDto.class);
        } catch (Exception e) {
            return order;
        }
    }
}
