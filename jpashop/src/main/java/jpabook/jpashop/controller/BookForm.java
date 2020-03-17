package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bind.annotation.Empty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;

@Getter @Setter
public class BookForm {

    private Long id;    //id 이대로 두어도 되는가?

    @NotEmpty
    private String name;
    @NotEmpty @Positive
    private int price;
    @NotEmpty @Positive
    private int stockQuantity;

    @NotEmpty
    private String author;
    @NotEmpty
    private String isbn;

    //@TODO: 더 아름답게 검사하는 방법 있을까?(ID가 NULL 인것을 강제한다, 혹시라도 POST로 잘못된 인자값 들어오는 것 방지)
    public boolean isIdEmpty(){
        return id == 0;
    }
}
