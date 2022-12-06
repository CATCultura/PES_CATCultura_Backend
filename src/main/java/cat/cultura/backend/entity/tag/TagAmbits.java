package cat.cultura.backend.entity.tag;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Ambits")
public class TagAmbits extends Tag {

    public TagAmbits() {
        super();
    }

    public TagAmbits(String name) {
        super(name, Type.AMBITS);
    }

}
