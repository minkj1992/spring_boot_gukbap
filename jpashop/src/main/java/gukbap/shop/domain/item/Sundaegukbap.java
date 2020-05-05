package gukbap.shop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class Sundaegukbap extends Item {

    private String chef;
    private String brand;
}
