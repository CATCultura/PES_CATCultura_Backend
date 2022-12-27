package cat.cultura.backend.entity;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Route")
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility= JsonAutoDetect.Visibility.ANY)
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long routeId;

    private String name;

    private String description;

    private String createdAt;

    @ManyToMany
    @JoinColumn(name = "events")
    private List<Event> routeEvents = new ArrayList<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void addEvents(List<Event> ev) {
        this.routeEvents.addAll(ev);
    }


}
