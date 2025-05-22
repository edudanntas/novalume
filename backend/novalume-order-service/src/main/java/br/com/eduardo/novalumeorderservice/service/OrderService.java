package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.orderitem.OrderItemDto;
import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.mapper.OrderMapper;
import br.com.eduardo.novalumeorderservice.model.Order;
import br.com.eduardo.novalumeorderservice.model.OrderItem;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import br.com.eduardo.novalumeorderservice.repository.OrderRepository;
import br.com.eduardo.novalumeorderservice.service.clients.ProductCatalogClient;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final ProductCatalogClient catalogClient;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final MessageProducer producer;

    public void createOrder(OrderCreateDto orderCreateDto) {
        Order newOrder = initializeOrder(orderCreateDto);
        addOrderItems(newOrder, orderCreateDto.items());
        finalizeOrder(newOrder);
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

    @Transactional
    protected void finalizeOrder(Order order) {
        order.calculateTotalAmount();
        orderRepository.save(order);
        producer.sendMessage(orderMapper.mapOrderEntityToMessage(order));
    }
}
