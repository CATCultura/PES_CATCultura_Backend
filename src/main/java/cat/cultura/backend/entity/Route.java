package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "Route")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long routeId;

    @ElementCollection
    @CollectionTable(name="RouteEvents", joinColumns=@JoinColumn(name="id"))
    @Column(name="route_events")
    private List<Event> routeEvents;

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long routeId) {
        this.routeId = routeId;
    }

    public List<Event> getRouteEvents() {
        return routeEvents;
    }

    public void setRouteEvents(List<Event> events) {
        this.routeEvents = events;
    }
}
