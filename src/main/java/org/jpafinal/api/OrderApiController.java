package org.jpafinal.api;

import lombok.RequiredArgsConstructor;
import org.jpafinal.domain.Order;
import org.jpafinal.repository.OrderRepository;
import org.jpafinal.repository.OrderSearch;
import org.springframework.boot.web.servlet.filter.OrderedRequestContextFilter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    /**
    * @author halfdev
    * @since 2020-01-15
    * 무한 루프에 빠지게 된다. 그래서 Member, OrderItem, Delivery 에 @JsonIgnore 를 선언해줬다.
     * 하지만, byteBuddy 에러를 만나게 된다. Lazy 라서 Froxy 로 갖고오는게 문제였다. Hibernate5Module @Bean 을 생성해주었다
    *
    */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll(new OrderSearch()); // findAll -> findAllByString
        for (Order order : all) {
            order.getMember().getName(); // LAZY 강제 초기화
            order.getDelivery().getAddress(); // LAZY 강제 초기화
        }
        return all;
        }
    }
