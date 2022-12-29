package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
@DiscriminatorValue("Organizer")
public class Organizer extends User {

    public Organizer(String username) {
        super(username, Role.ORGANIZER);
    }

    public Organizer(String username, Role role) {
        super(username, role);
        if (role != Role.ORGANIZER && role != Role.ADMIN) {
            throw new AssertionError("Wrong user type");
        }

    }
    public Organizer() {
        super();
    }

    @OneToMany(mappedBy="organizer")
    private List<Event> organizedEvents = new ArrayList<>();

    @Override
    public List<Event> getOrganizedEvents() {
        return organizedEvents;
    }

    @Override
    public void setOrganizedEvents(List<Event> organizedEvents) {
        this.organizedEvents = organizedEvents;
    }
}
