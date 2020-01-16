package org.jpafinal.api;

import lombok.RequiredArgsConstructor;
import org.jpafinal.domain.Order;
import org.jpafinal.dto.OrderDto;
import org.jpafinal.repository.OrderRepository;
import org.jpafinal.repository.OrderSearch;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
* @author halfdev
* @since 2020-01-15
* xToOne 관계 성능 최적화
 * Order -> Member ( @ManyToOne )
 * Order -> Delivery ( @OneToOne )
*/
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch()); // findAll -> findAllByString
        // for 문으로 돌려도 되고 Stream 으로 돌려도 됨
        for (Order order : all) {
            order.getMember().getName(); // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
        }
            return all;
        }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {

        List<Order> orders = orderRepository.findAll(new OrderSearch());

        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }

    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();

        // order reference value 체크. jpa 에서는 id 가 똑같은 참조값도 똑같다.
//        for (Order order : orders) {
//            System.out.println("order ref="+ order + " id=" + order.getId());
//
//        }
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());

        return result;
    }


}
