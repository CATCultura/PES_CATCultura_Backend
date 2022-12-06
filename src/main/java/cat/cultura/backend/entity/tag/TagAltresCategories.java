package cat.cultura.backend.entity.tag;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("AltresCategories")
public class TagAltresCategories extends Tag {

    public TagAltresCategories() {
        super();
    }

    public TagAltresCategories(String name) {
        super(name, Type.ALTRES_CATEGORIES);
    }
}
