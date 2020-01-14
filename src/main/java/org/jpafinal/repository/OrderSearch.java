package org.jpafinal.repository;

import lombok.Getter;
import lombok.Setter;
import org.jpafinal.domain.OrderStatus;

@Getter
@Setter
public class OrderSearch {

    private String memberName; //회원이름
    private OrderStatus orderStatus; //ORDER CANCEL
}
