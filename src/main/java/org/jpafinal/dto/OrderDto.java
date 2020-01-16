package org.jpafinal.dto;

import lombok.Data;
import org.jpafinal.domain.Address;
import org.jpafinal.domain.Order;
import org.jpafinal.domain.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName(); // Lazy 초기화 ( 영속성 컨텍스트 안에 name 이 없으면 DB 쿼리 날림 )
        orderDate = order.getOrderDate();
        orderStatus = order.getOrderStatus();
        address = order.getDelivery().getAddress(); // Lazy 초기화 ( 영속성 컨텍스트 안에 address 가 없으면 DB 쿼리 날림 )
        orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(Collectors.toList());
    }
}
