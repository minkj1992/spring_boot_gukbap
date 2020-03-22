package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.dto.OrderListResponse;
import jpabook.jpashop.dto.OrderQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.query.OrderQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;


    /**
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록
     */
    @GetMapping("/api/v1/orders")
    public OrderListResponse ordersV1() {
        List<Order> all = orderRepository.findAll();
        for (Order order : all) {
            order.getMember().getName();    //LAZY 강제 초기화
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());  // LAZY
        }
        return new OrderListResponse(all);
    }

    @GetMapping("api/v2/orders")
    public OrderListResponse ordersV2() {
        List<Order> orders = orderRepository.findAll();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return new OrderListResponse(result);
    }

    // OrderRepository에 findAllWithItem()추가
    @GetMapping("api/v3/orders")
    public OrderListResponse ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return new OrderListResponse(result);
    }

    /**
     * V3.1: 엔티티를 조회한 뒤 DTO로 변환 / 페이징 처리
     * - ToOne 관계는 fetch join
     * - 컬렉션 관계는 hibernate.default_batch_fetch_size, @BatchSize로 최적화
     */
    @GetMapping("api/v3.1/orders")
    public OrderListResponse ordersV3_page(@RequestParam(value = "offset", defaultValue = "0") int offset,
                                           @RequestParam(value = "limit", defaultValue = "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDeliver(offset, limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(toList());
        return new OrderListResponse(result);
    }

    /**
     * V4: JPA에서 DTO 직접 조회
     */
    @GetMapping("/api/v4/orders")
    public OrderListResponse ordersV4() {
        return new OrderListResponse(orderQueryRepository.findOrderQueryDtos());
    }
}
