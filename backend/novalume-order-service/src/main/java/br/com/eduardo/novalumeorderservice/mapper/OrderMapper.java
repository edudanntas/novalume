package br.com.eduardo.novalumeorderservice.mapper;

import br.com.eduardo.novalumeorderservice.dto.order.OrderCreateDto;
import br.com.eduardo.novalumeorderservice.dto.order.OrderMessage;
import br.com.eduardo.novalumeorderservice.dto.order.OrderResponseDto;
import br.com.eduardo.novalumeorderservice.model.Order;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order mapOrderCreateDtoToOrderEntity(OrderCreateDto orderCreateDto);

    OrderResponseDto mapOrderEntityToOrderResponseDto(Order order);

    OrderMessage mapOrderEntityToMessage(Order order);
}
