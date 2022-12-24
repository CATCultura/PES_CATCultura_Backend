package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.RouteDto;
import cat.cultura.backend.entity.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class RouteMapper {
    @Autowired
    EventMapper eventMapper;

    public RouteDto convertRouteToDto(Route route) {
        RouteDto routeDto = new RouteDto();
        routeDto.setRouteId(route.getRouteId());
        routeDto.setRouteEvents(route.getRouteEvents().stream().map(eventMapper::convertEventToDto).toList());
        routeDto.setName(route.getName());
        routeDto.setDescription(route.getDescription());
        routeDto.setCreatedAt(route.getCreatedAt());
        return routeDto;
    }

    public Route convertRouteDtoToEntity(RouteDto routeDto) {
        Route route = new Route();
        route.setRouteEvents(routeDto.getRouteEvents().stream().map(eventMapper::convertEventDtoToEntity).toList());
        route.setName(routeDto.getName());
        route.setDescription(routeDto.getDescription());
        return route;
    }
}
