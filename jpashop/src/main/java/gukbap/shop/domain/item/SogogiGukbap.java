package gukbap.shop.domain.item;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class SogogiGukbap extends Item{

    private String chef;
    private String brand;
}
