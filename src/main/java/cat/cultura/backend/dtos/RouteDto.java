package cat.cultura.backend.dtos;
import java.util.List;

public class RouteDto {
    private Long routeId;
    private List<EventDto> routeEvents;

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
}
