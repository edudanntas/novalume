package br.com.eduardo.novalumeorderservice.controller;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        orderService.createOrder(orderCreateDto);
        return ResponseEntity.ok("Order created");
    }
}
