package jpabook.jpashop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OrderListResponse<T> {
    private T data;
}
