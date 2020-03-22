package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.repository.ItemRepository;
import lombok.NoArgsConstructor;
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

        String book1 = "해리포터";
        int price1 = 10000;
        int stockQuantity1 = 100;

        String book2 = "반지의 제왕";
        int price2 = 20000;
        int stockQuantity2 = 200;


        for (int i = 1; i < numberOfMember; i++) {
            initService.dbInit(name + i, city + i, street + i, zipcode + i, book1 + i, price1, stockQuantity1,book2+i,price2,stockQuantity2);
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;



        public void dbInit(String name, String city, String street, String zipcode, String book_one, int price, int stockQuantity, String book_second, int price2, int stockQuantity2) {
            Member member = createMember(name, city, street, zipcode);
            em.persist(member);
            Book book1 = createBook(book_one, price, stockQuantity);
            em.persist(book1);

            Book book2 = createBook(book_second, price2, stockQuantity2);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, price, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, price2, 2);
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

        private Book createBook(String name, int price, int stockQuantity) {
            Book book = new Book();
            book.setName(name);
            book.setPrice(price);
            book.setStockQuantity(stockQuantity);
            return book;
        }

        private Delivery createDelivery(Member member) {
            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            return delivery;
        }
    }
}