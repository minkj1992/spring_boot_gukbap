package gukbap.shop;

import gukbap.shop.domain.*;
import gukbap.shop.domain.item.Sundaegukbap;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDb {
    private final InitService initService;

    @PostConstruct
    public void init() {
        int numberOfMember = 11;
        String name = "회원";
        String city = "도시";
        String street = "거리";
        String zipcode = "거리번호";

        String gukbap1 = "할매국밥";
        int price1 = 10000;
        int stockQuantity1 = 100;

        String gukbap2 = "아빠국밥";
        int price2 = 20000;
        int stockQuantity2 = 200;


        for (int i = 1; i < numberOfMember; i++) {
            initService.dbInit(name + i, city + i, street + i, zipcode + i, gukbap1 + i, price1, stockQuantity1,gukbap2+i,price2,stockQuantity2);
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;



        public void dbInit(String name, String city, String street, String zipcode, String gukbap1, int price, int stockQuantity, String gukbap2, int price2, int stockQuantity2) {
            Member member = createMember(name, city, street, zipcode);
            em.persist(member);
            Sundaegukbap sundaegukbap1 = createSundaegukbap(gukbap1, price, stockQuantity);
            em.persist(sundaegukbap1);

            Sundaegukbap sundaegukbap2 = createSundaegukbap(gukbap2, price2, stockQuantity2);
            em.persist(sundaegukbap2);

            OrderItem orderItem1 = OrderItem.createOrderItem(sundaegukbap1, price, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(sundaegukbap2, price2, 2);
            Order order = Order.createOrder(member, createDelivery(member), orderItem1, orderItem2);
            em.persist(order);
        }


        private Member createMember(String name, String city, String street,
                                    String zipcode) {
            Member member = new Member();
            member.setName(name);
            member.setAddress(new Address(city, street, zipcode));
            return member;
        }

        private Sundaegukbap createSundaegukbap(String name, int price, int stockQuantity) {
            Sundaegukbap sundaegukbap = new Sundaegukbap();
            sundaegukbap.setName(name);
            sundaegukbap.setPrice(price);
            sundaegukbap.setStockQuantity(stockQuantity);
            return sundaegukbap;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}