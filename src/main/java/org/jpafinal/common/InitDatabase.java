package org.jpafinal.common;

import lombok.RequiredArgsConstructor;
import org.jpafinal.domain.*;
import org.jpafinal.domain.item.Book;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * @author halfdev
 * @since 2020-01-14
 * 주문 : 2 개
 * UserA
 * JPA1 Book
 * JPA2 Book
 * UserB
 * SPRING1 Book
 * SPRING2 Book
 */
@Component
@RequiredArgsConstructor
public class InitDatabase {

    private final InitService initService;

    @PostConstruct // 별도로 생성해준 Spring Bean 이 모두 올라오면 아래 로직들을 호출해준다.
    public void init() {
        initService.dbInitA();
        initService.dbInitB();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInitA() {
            Member member = createMember("userA", "seoul", "1", "1111");
            em.persist(member);

            Book book1 = createBook("JPA1 Book", 10000, 100);
            em.persist(book1);

            Book book2 = createBook("JPA2 Book", 20000, 200);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        public void dbInitB() {
            Member member = createMember("userB", "busan", "2", "2222");
            em.persist(member);

            Book book1 = createBook("SPRING Book1", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING Book2", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member);
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);

        }

        // Extract Method : Ctrl+Alt+M
        private Book createBook(String name, int price, int stockQuantity) {
            Book book1 = new Book();
            book1.setName(name);
            book1.setPrice(price);
            book1.setStockQuantity(stockQuantity);
            return book1;
        }

        private Member createMember(String name, String city, String street, String zipCode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipCode));
            return member;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }

    }

}

