package org.jpafinal.dto;

import lombok.Data;
import org.jpafinal.domain.Address;
import org.jpafinal.domain.OrderStatus;

import java.time.LocalDateTime;

/**
* @author halfdev
* @since 2020-01-19
* 기존에 만들어 두었던 OrderItemQueryDto 와 OrderQueryDto 의 필드를
* 모두 가져와서 한 번에 조인 후 쿼리를 날린다.
*/
@Data
public class OrderFlatDto {

    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;

    private String itemName;
    private int orderPrice;
    private int count;

    public OrderFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.name = name;
        this.orderDate = orderDate;
        this.orderStatus = orderStatus;
        this.address = address;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
