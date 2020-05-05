package gukbap.shop.service;

import gukbap.shop.domain.Address;
import gukbap.shop.domain.Member;
import gukbap.shop.domain.Order;
import gukbap.shop.domain.OrderStatus;
import gukbap.shop.domain.item.Sundaegukbap;
import gukbap.shop.domain.item.Item;
import gukbap.shop.exception.NotEnoughStockException;
import gukbap.shop.repository.OrderRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @PersistenceContext
    EntityManager em;

    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception{

        //given
        Member member = createMember();
        Item item = createGukbap();
        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(),item.getId(),orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 상태 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량",10000*orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.",10-orderCount, item.getStockQuantity());

    }

    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception{
        //given
        Member member = createMember();
        Item item = createGukbap();
        int orderCount = 11;
        //when
        orderService.order(member.getId(),item.getId(),orderCount);

        //then

        fail("재고 부족으로 예외 발생해야 한다.");
    }

    @Test
    public void 주문취소() throws Exception{
        //given
        Member member = createMember();
        Item item = createGukbap();
        int orderCount = 2;
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 CANCEL",OrderStatus.CANCEL,getOrder.getStatus());
        assertEquals("주문 취소된 상품은 그 만큼 재고가 증가해야 한다.",10,item.getStockQuantity());
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        em.persist(member);
        return member;
    }

    private Sundaegukbap createGukbap() {
        Sundaegukbap sundaegukbap = new Sundaegukbap();
        sundaegukbap.setName("제주도 개발자");
        sundaegukbap.setStockQuantity(10);
        sundaegukbap.setPrice(10000);
        em.persist(sundaegukbap);
        return sundaegukbap;
    }


}