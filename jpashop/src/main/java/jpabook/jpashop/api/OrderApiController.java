package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.dto.OrderDto;
import jpabook.jpashop.dto.OrderListResponse;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;


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
            orderItems.stream().forEach(o->o.getItem().getName());  // LAZY
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
                .map(o-> new OrderDto())
                .collect(toList());
        return new OrderListResponse(result);
    }




}
