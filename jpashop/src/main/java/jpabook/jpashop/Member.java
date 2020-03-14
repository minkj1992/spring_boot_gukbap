package jpabook.jpashop;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // DB 칼럼명 (H2) -> 테이블은 타입이 없지만 엔티티는 클래스 타입이 존재하니 간단한 변수명 사용.
    private Long id;    //엔티티 식별자

    private String name;

    @Embedded
    private Address address;    //임베디드 타입

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); // mappdedBy에 들어가는 인자는, 연관관계주인이 가지고 있는 변수명 (fk가 된다 DB상)

}
