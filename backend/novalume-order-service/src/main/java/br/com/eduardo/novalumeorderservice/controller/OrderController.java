package br.com.eduardo.novalumeorderservice.controller;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderResponseDto;
import br.com.eduardo.novalumeorderservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderCreateDto orderCreateDto) {
        orderService.createOrder(orderCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order created");
    }

    @GetMapping(value = "/{orderId}")
    public ResponseEntity<OrderResponseDto> getORderById(@PathVariable UUID orderId,
                                                         @RequestParam(required = false) String fields){
        return ResponseEntity.status(HttpStatus.OK).body(orderService.getOderById(orderId, fields));
    }
}
