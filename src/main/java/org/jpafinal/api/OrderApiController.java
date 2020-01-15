package org.jpafinal.api;

import lombok.RequiredArgsConstructor;
import org.jpafinal.domain.Order;
import org.jpafinal.dto.OrderDto;
import org.jpafinal.repository.OrderRepository;
import org.jpafinal.repository.OrderSearch;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
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

    /**
    * @author halfdev
    * @since 2020-01-16
    * v1 보다는 개선 되었지만, 문제가 있다.
    * LAZY Loading 으로 인한 쿼리가 너무 많이 호출된다.
    * OrderDto 에는 Lazy 초기화가 발생한다.
     * 참고로 Lazy Loading 은 영속성 컨텍스트에서 먼저 조회한다.
    */
    @GetMapping("/api/v2/orders")
    public List<OrderDto> ordersV2() {
        // Order 2개
        // N + 1 문제 발생 (첫 번째 쿼리 결과로 N 만큼 추가 실행된다. 여기서 N=2)
        // 1 + 회원 N + 배송 N -> 5 번의 쿼리가 나간다.
        List<Order> orders = orderRepository.findAll(new OrderSearch()); // findAll -> findAllByString

        //
        List<OrderDto> result = orders.stream()
                .map(OrderDto::new) // o -> new OrderDto(o) 이것과 같음
                .collect(Collectors.toList());

        return result;
    }


    }
