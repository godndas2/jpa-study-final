package org.jpafinal.dto;

import lombok.Getter;
import org.jpafinal.domain.OrderItem;

@Getter
public class OrderItemDto {

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemDto(OrderItem orderItem) {
        itemName = orderItem.getItem().getName();
        orderPrice = orderItem.getOrderPrice();
        count = orderItem.getCount();
    }

}
