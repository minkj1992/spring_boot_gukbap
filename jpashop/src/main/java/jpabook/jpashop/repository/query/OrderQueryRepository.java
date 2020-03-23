package jpabook.jpashop.repository.query;

import jpabook.jpashop.dto.OrderFlatDto;
import jpabook.jpashop.dto.OrderItemDto;
import jpabook.jpashop.dto.OrderItemQueryDto;
import jpabook.jpashop.dto.OrderQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

    private final EntityManager em;

    /**
     * 컬렉션은 별도 조회
     * Query: Root 1 -> Collection N
     * 단건 조회에서 많이 사용하는 방식
     */
    public List<OrderQueryDto> findOrderQueryDtos() {
        // 루트 조회 ( toOne 코드 한번에 조회 )
        List<OrderQueryDto> result = findOrders();

        // 컬렉션 추가
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    /**
     * xToOne 관계 조회
     */
    private List<OrderQueryDto> findOrders() {
        return em.createQuery("select new jpabook.jpashop.dto.OrderQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d", OrderQueryDto.class)
                .getResultList();
    }

    /**
     * xToMany 관계 조회
     */
    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return em.createQuery("select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id = : orderId", OrderItemQueryDto.class)
                .setParameter("orderId", orderId)
                .getResultList();

    }


    /**
     * for V5
     */
    public List<OrderQueryDto> findAllByDto_optimization() {
        List<OrderQueryDto> result = findOrders();  //xToOne

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));


        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(toList());
    }


    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        List<OrderItemQueryDto> orderItems = em.createQuery("select new jpabook.jpashop.dto.OrderItemQueryDto(oi.order.id, i.name, oi.orderPrice, oi.count)" +
                " from OrderItem oi" +
                " join oi.item i" +
                " where oi.order.id in :orderIds", OrderItemQueryDto.class)
                .setParameter("orderIds", orderIds)
                .getResultList();
        return orderItems.stream()
                .collect(groupingBy(o -> o.getOrderId()));
    }


    /**
     * for V6 : flat version
     */
    public List<OrderFlatDto> findAllByDto_flat() {
        return em.createQuery("select new jpabook.jpashop.dto.OrderFlatDto(o.id, m.name, o.orderDate, o.status, d.address, i.name, oi.orderPrice, oi.count)" +
                " from Order o" +
                " join o.member m" +
                " join o.delivery d" +
                " join o.orderItems oi" +
                " join oi.item i", OrderFlatDto.class)
                .getResultList();
    }
}
