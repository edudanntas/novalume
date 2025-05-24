package br.com.eduardo.novalumeorderservice.service;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderResponseDto;
import br.com.eduardo.novalumeorderservice.dto.orderitem.OrderItemDto;
import br.com.eduardo.novalumeorderservice.dto.product.ProductDto;
import br.com.eduardo.novalumeorderservice.infra.exception.custom.OrderNotFoundException;
import br.com.eduardo.novalumeorderservice.infra.exception.custom.ProductCatalogUnavailableException;
import br.com.eduardo.novalumeorderservice.mapper.OrderMapper;
import br.com.eduardo.novalumeorderservice.model.Order;
import br.com.eduardo.novalumeorderservice.model.OrderItem;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import br.com.eduardo.novalumeorderservice.repository.OrderRepository;
import br.com.eduardo.novalumeorderservice.service.clients.ProductCatalogClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private ProductCatalogClient catalogClient;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @Mock
    private OrderEventProducer producer;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private OrderService orderService;

    private OrderCreateDto orderCreateDto;
    private OrderResponseDto orderResponseDto;
    private Order order;
    private ProductDto productDto;
    private UUID orderId;
    private UUID customerId;

    @BeforeEach
    void setUp() {
        orderId = UUID.randomUUID();
        customerId = UUID.randomUUID();

        productDto = new ProductDto(
                "prod-1",
                "Test Product",
                100.00,
                true
        );

        OrderItemDto orderItemDto = new OrderItemDto(
                "prod-1",
                2
        );

        orderCreateDto = new OrderCreateDto(
                customerId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(orderItemDto)
        );

        order = new Order();
        order.setId(orderId);
        order.setCustomerId(customerId);
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());

        OrderItem orderItem = new OrderItem();
        orderItem.setProductId("prod-1");
        orderItem.setProductName("Test Product");
        orderItem.setQuantity(2);
        orderItem.setUnitPrice(BigDecimal.valueOf(100.00));
        orderItem.calculateSubtotal();
        order.addItem(orderItem);
        order.calculateTotalAmount();

        orderResponseDto = new OrderResponseDto(
                orderId,
                LocalDateTime.now(),
                "Cliente Teste",
                OrderStatus.PENDING,
                List.of(),
                BigDecimal.valueOf(200.00)
        );
    }

    @Test
    void criacaoDePedidoComSucesso() {
        when(catalogClient.getProductById("prod-1")).thenReturn(productDto);

        orderService.createOrder(orderCreateDto);

        verify(orderRepository).save(any(Order.class));
        verify(producer).sendMessage(any());
    }

    @Test
    void criacaoDePedidoComMultiplosProdutos() {
        OrderItemDto item1 = new OrderItemDto("prod-1", 2);
        OrderItemDto item2 = new OrderItemDto("prod-2", 3);
        OrderCreateDto pedidoMultiplo = new OrderCreateDto(
                customerId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(item1, item2)
        );

        ProductDto produto1 = new ProductDto("prod-1", "Product 1", 100.00, true);
        ProductDto produto2 = new ProductDto("prod-2", "Product 2", 50.00, true);

        when(catalogClient.getProductById("prod-1")).thenReturn(produto1);
        when(catalogClient.getProductById("prod-2")).thenReturn(produto2);

        orderService.createOrder(pedidoMultiplo);

        verify(orderRepository).save(any(Order.class));
        verify(producer).sendMessage(any());
    }

    @Test
    void criacaoDePedidoFalhaQuandoCatalogoIndisponivel() {
        when(catalogClient.getProductById(any())).thenThrow(new ProductCatalogUnavailableException("Serviço indisponível"));

        assertThrows(ProductCatalogUnavailableException.class, () -> {
            orderService.createOrder(orderCreateDto);
        });

        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void retornaPedidoQuandoEncontrado() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.mapOrderEntityToOrderResponseDto(order)).thenReturn(orderResponseDto);

        OrderResponseDto resultado = orderService.getOderById(orderId, null);

        assertNotNull(resultado);
        assertEquals(orderResponseDto, resultado);
    }

    @Test
    void lancaExcecaoQuandoPedidoNaoEncontrado() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        assertThrows(OrderNotFoundException.class, () -> {
            orderService.getOderById(orderId, null);
        });
    }

    @Test
    void filtragemDeCamposNoRetornoDoPedido() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.mapOrderEntityToOrderResponseDto(order)).thenReturn(orderResponseDto);

        Map<String, Object> mapaFiltrado = new HashMap<>();
        mapaFiltrado.put("id", orderId);
        mapaFiltrado.put("status", OrderStatus.PENDING);

        OrderResponseDto dtoFiltrado = new OrderResponseDto(orderId, null, null, OrderStatus.PENDING, null, null);

        when(objectMapper.convertValue(eq(orderResponseDto), eq(Map.class))).thenReturn(new HashMap<>(Map.of(
                "id", orderId,
                "customerId", customerId,
                "customerName", "Cliente Teste",
                "status", OrderStatus.PENDING,
                "totalAmount", BigDecimal.valueOf(200.00)
        )));

        when(objectMapper.convertValue(any(Map.class), eq(OrderResponseDto.class))).thenReturn(dtoFiltrado);

        OrderResponseDto resultado = orderService.getOderById(orderId, "id,status");

        assertNotNull(resultado);
        assertEquals(dtoFiltrado, resultado);
    }

    @Test
    void listagemDeTodosPedidosDoCliente() {
        Order pedido1 = new Order();
        pedido1.setId(UUID.randomUUID());
        pedido1.setCustomerId(customerId);

        Order pedido2 = new Order();
        pedido2.setId(UUID.randomUUID());
        pedido2.setCustomerId(customerId);

        List<Order> listaPedidos = List.of(pedido1, pedido2);

        when(orderRepository.findAllByCustomerId(customerId)).thenReturn(listaPedidos);

        OrderResponseDto dto1 = new OrderResponseDto(pedido1.getId(), LocalDateTime.now(), "Cliente", OrderStatus.PENDING,
                List.of(), BigDecimal.TEN);
        OrderResponseDto dto2 = new OrderResponseDto(pedido2.getId(), LocalDateTime.now(), "Cliente", OrderStatus.PENDING,
                List.of(), BigDecimal.TEN);

        when(orderMapper.mapListOfOrderEntityToOrderResponseDtoList(listaPedidos)).thenReturn(List.of(dto1, dto2));

        List<OrderResponseDto> resultado = orderService.getAllOrdersFromCustomer(customerId, null);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
    }

    @Test
    void retornaListaVaziaQuandoClienteNaoTemPedidos() {
        when(orderRepository.findAllByCustomerId(customerId)).thenReturn(List.of());
        when(orderMapper.mapListOfOrderEntityToOrderResponseDtoList(List.of())).thenReturn(List.of());

        List<OrderResponseDto> resultado = orderService.getAllOrdersFromCustomer(customerId, null);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    @Test
    void calculaValorTotalDoPedidoCorretamente() {
        OrderItemDto item1 = new OrderItemDto("prod-1", 2);
        OrderItemDto item2 = new OrderItemDto("prod-2", 3);
        OrderCreateDto multipleOrder = new OrderCreateDto(
                customerId,
                UUID.randomUUID(),
                UUID.randomUUID(),
                List.of(item1, item2)
        );

        ProductDto product1 = new ProductDto("prod-1", "Product 1", 100.00, true);
        ProductDto product2 = new ProductDto("prod-2", "Product 2", 50.00, true);

        when(catalogClient.getProductById("prod-1")).thenReturn(product1);
        when(catalogClient.getProductById("prod-2")).thenReturn(product2);

        ArgumentCaptor<Order> orderCaptor = ArgumentCaptor.forClass(Order.class);

        orderService.createOrder(multipleOrder);

        verify(orderRepository).save(orderCaptor.capture());
        Order pedidoSalvo = orderCaptor.getValue();

        // O total deve ser (2 * 100) + (3 * 50) = 200 + 150 = 350
        assertEquals(new BigDecimal("350.0"), pedidoSalvo.getTotalAmount());
    }

    @Test
    void trataErroNaFiltragemDeCampos() {
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderMapper.mapOrderEntityToOrderResponseDto(order)).thenReturn(orderResponseDto);

        when(objectMapper.convertValue(eq(orderResponseDto), eq(Map.class))).thenThrow(new IllegalArgumentException(
                "Error to get fields"));

        OrderResponseDto resultado = orderService.getOderById(orderId, "invalidField");

        assertEquals(orderResponseDto, resultado);
    }
}