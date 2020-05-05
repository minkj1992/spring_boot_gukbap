package gukbap.shop.domain.item;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;


@Entity
@DiscriminatorValue("M")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DwaejiGukbap extends Item {

    private String chef;
    private String brand;
}
