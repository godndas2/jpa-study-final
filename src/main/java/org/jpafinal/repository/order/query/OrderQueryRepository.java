package org.jpafinal.repository.order.query;

import lombok.RequiredArgsConstructor;
import org.jpafinal.dto.OrderItemQueryDto;
import org.jpafinal.dto.OrderQueryDto;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    public List<OrderQueryDto> findOrderQueryDto() {
        List<OrderQueryDto> result = findOrders(); // Orders 를 가져오고

        // Collection 은 따로 채워준다
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });
        return result;
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery(
                "select new org.jpafinal.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                   " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id = :orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();
    }

    public List<OrderQueryDto> findOrders() {
        return em.createQuery(
                "select new org.jpafinal.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.orderStatus, d.address)" +
                        " from Order o" +
                        " join o.member m" +
                        " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * @author halfdev
     * @since 2020-01-18
     * Query 를 한 번 날리고, Memory 에서 Map 으로 다 가져와서 result.forEach 에서 set 을 해준다.
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();

        List<Long> orderIds = result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());

        List<OrderItemQueryDto> orderItems = em.createQuery(
                "select new org.jpafinal.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                        " from OrderItem oi" +
                        " join oi.item i" +
                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();

        // orderId 를 기준으로 Map 으로 바꾼다. Key : getOrderId , Value : OrderItemQueryDto
        Map<Long, List<OrderItemQueryDto>> orderItemMap = orderItems.stream()
                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    /**
    * @author halfdev
    * @since 2020-01-18
    * findAllByDto_optimization() Refactoring Version
    */
//    public List<OrderQueryDto> findAllByDto_optimizationV2() {
//        List<OrderQueryDto> result = findOrders();
//
//        List<Long> orderIds = toOrderIds(result);
//
//        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(orderIds);
//
//        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));
//
//        return result;
//    }
//
//    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
//        List<OrderItemQueryDto> orderItems = em.createQuery(
//                "select new org.jpafinal.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
//                        " from OrderItem oi" +
//                        " join oi.item i" +
//                        " where oi.order.id in :orderIds", OrderItemQueryDto.class)
//                .setParameter("orderIds", orderIds)
//                .getResultList();
//
//        // orderId 를 기준으로 Map 으로 바꾼다. Key : getOrderId , Value : OrderItemQueryDto
//        return orderItems.stream()
//                .collect(Collectors.groupingBy(orderItemQueryDto -> orderItemQueryDto.getOrderId()));
//    }
//
//    private List<Long> toOrderIds(List<OrderQueryDto> result) {
//        return result.stream()
//                    .map(o -> o.getOrderId())
//                    .collect(Collectors.toList());
//    }


}
