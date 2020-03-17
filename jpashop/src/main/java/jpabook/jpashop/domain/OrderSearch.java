package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderSearch {

    private String memberName;
    private OrderStatus orderStatus;

}
