package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.dto.OrderListResponse;
import jpabook.jpashop.dto.SimpleOrderDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.query.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static java.util.stream.Collectors.*;

/**
 * 주문 + 배송정보 + 회원을 조회하는 API
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;



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

    /**
     * V2: DTO 변환
     * - 단점: V1과 마찬가지로 1+N+N 쿼리 문제 발생
     */
    @GetMapping("api/v2/simple-orders")
    public OrderListResponse ordersV2() {
        List<Order> orders = orderRepository.findAll();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return new OrderListResponse(result);
    }

    /**
     * V3: DTO + fetch join
     * - toOne관계이므로 (1+N) -> 1 최적화 가능
     */
    @GetMapping("api/v3/simple-orders")
    public OrderListResponse ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDeliver();
        List<SimpleOrderDto> result = orders.stream()
                .map(o -> new SimpleOrderDto(o))
                .collect(toList());
        return new OrderListResponse(result);
    }

    /**
     * V4: JPA -> DTO 바로 조회 (/repository에 query 폴더 생성)
     * - 1 query
     * - DB query에서 원하는 select 인자 선택 가능
     */
    @GetMapping("api/v4/simple-orders")
    public OrderListResponse ordersV4() {
        return new OrderListResponse(orderSimpleQueryRepository.findOrderDtos());
    }
}
