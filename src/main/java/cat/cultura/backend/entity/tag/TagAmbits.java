package cat.cultura.backend.entity.tag;

import cat.cultura.backend.entity.Event;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.Set;

@Entity
@DiscriminatorValue("Ambits")
public class TagAmbits extends Tag {

    public TagAmbits() {
        super();
    }

    public TagAmbits(String name) {
        super(name, Type.AMBITS);
    }


    public Set<Event> getEventList() {
        return eventList;
    }


    public void setEventList(Set<Event> eventList) {
        this.eventList = eventList;
    }

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tagsAmbits")
    protected Set<Event> eventList;

}
