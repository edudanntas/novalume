package br.com.eduardo.novalumeorderservice.mapper;

import br.com.eduardo.novalumeorderservice.dto.order.CreatePaymentEventDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderResponseDto;
import br.com.eduardo.novalumeorderservice.model.Order;
import br.com.eduardo.novalumeorderservice.model.enums.OrderEvent;
import br.com.eduardo.novalumeorderservice.model.enums.OrderStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapOrderCreateDtoToOrderEntity(OrderCreateDto orderCreateDto);

    OrderResponseDto mapOrderEntityToOrderResponseDto(Order order);

    List<OrderResponseDto> mapListOfOrderEntityToOrderResponseDtoList(List<Order> orders);

    @Mapping(source = "id", target = "orderId")
    @Mapping(target = "eventType", expression = "java(determineOrderEvent(order))")
    CreatePaymentEventDto mapOrderEntityToMessage(Order order);

    default OrderEvent determineOrderEvent(Order order){
        if (order.getStatus() == OrderStatus.PENDING){
            return OrderEvent.ORDER_CREATED;
        }

        return OrderEvent.ORDER_PROCESSED;
    }
}
