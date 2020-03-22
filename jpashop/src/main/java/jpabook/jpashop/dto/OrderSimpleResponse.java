package jpabook.jpashop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderSimpleResponse<T> {
    private T data;
}
