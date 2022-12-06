package cat.cultura.backend.entity.tag;

import cat.cultura.backend.entity.Event;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("Categories")
public class TagCategories extends Tag{

    public TagCategories() {
        super();
    }

    public TagCategories(String name) {
        super(name, Type.CATEGORIES);
    }


    public Set<Event> getEventList() {
        return eventList;
    }


    public void setEventList(Set<Event> eventList) {
        this.eventList = eventList;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tagsCateg")
    protected Set<Event> eventList;

}
