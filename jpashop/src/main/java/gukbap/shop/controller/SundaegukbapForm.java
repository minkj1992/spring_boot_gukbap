package gukbap.shop.controller;

import lombok.Getter;
import lombok.Setter;


@Getter @Setter
public class SundaegukbapForm {

    private Long id;    //id 이대로 두어도 되는가?

    private String name;

//    @PositiveOrZero(message = "가격은 음수가 될 수 없습니다.")
    private int price;
//    @Positive(message = "수량은 1개 이상이어야 합니다.")
    private int stockQuantity;

//    @NotEmpty(message = "Chef 이름은 필수입니다.")
    private String chef;
//    @NotEmpty(message = "brand은 필수입니다.")
    private String brand;

//    //@TODO: 더 아름답게 검사하는 방법 있을까?(ID가 NULL 인것을 강제한다, 혹시라도 POST로 잘못된 인자값 들어오는 것 방지)
//    public boolean isIdEmpty(){
//        return id == 0;
//    }
}
