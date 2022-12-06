package cat.cultura.backend.entity.tag;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("Categories")
public class TagCategories extends Tag{

    public TagCategories() {
        super();
    }

    public TagCategories(String name) {
        super(name, Type.CATEGORIES);
    }

}
