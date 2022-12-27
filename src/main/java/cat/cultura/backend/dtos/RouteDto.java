package cat.cultura.backend.dtos;
import java.util.ArrayList;
import java.util.List;

public class RouteDto {
    private Long routeId;

    private String name;

    private String description;

    private String createdAt;

    private List<Long> eventIds = new ArrayList<>();

    private List<EventDto> routeEvents = new ArrayList<>();

    public Long getRouteId() {
        return routeId;
    }

    public void setRouteId(Long id) {
        this.routeId = id;
    }

    public List<EventDto> getRouteEvents() {
        return routeEvents;
    }

    public void setRouteEvents(List<EventDto> events) {
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

    public List<Long> getEventIds() {
        return eventIds;
    }

    public void setEventIds(List<Long> eventIds) {
        this.eventIds = eventIds;
    }
}
