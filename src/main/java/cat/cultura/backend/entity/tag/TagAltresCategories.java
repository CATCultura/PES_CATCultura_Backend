package cat.cultura.backend.entity.tag;

import cat.cultura.backend.entity.Event;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("AltresCategories")
public class TagAltresCategories extends Tag {

    public TagAltresCategories() {
        super();
    }

    public TagAltresCategories(String name) {
        super(name, Type.ALTRES_CATEGORIES);
    }


    public Set<Event> getEventList() {
        return eventList;
    }


    public void setEventList(Set<Event> eventList) {
        this.eventList = eventList;
    }

    @ManyToMany (fetch = FetchType.LAZY, mappedBy = "tagsAltresCateg")
    protected Set<Event> eventList;
}
