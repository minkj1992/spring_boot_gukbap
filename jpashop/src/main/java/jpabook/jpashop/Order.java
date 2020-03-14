package jpabook.jpashop;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") //Order_by와 같은 예약어 때문에, 관례상 주문 table은 ORDERS를 자주 쓴다.
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  //Eager는 fetch시 관계있는 녀석 다 가져옴
    @JoinColumn(name = "member_id") //FK 부여, 연관관계의 주인
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)

    private Delivery delivery;

    private LocalDateTime orderDate;

    private OrderStatus status; //[ORDER, CANCEL]


}
