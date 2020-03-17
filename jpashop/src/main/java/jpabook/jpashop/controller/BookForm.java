package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BookForm {

    private Long id;    //id 이대로 두어도 되는가?

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;
}
