package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 주문 + 배송정보 + 회원을 조회하는 API
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;



    /**
     * V1: 엔티티 직접 노출
     * - Hibernate5Module 등록, LAZY = null
     * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName();    // LAZY
            order.getDelivery().getAddress();
        }
        return all;
    }
}
