package cat.cultura.backend.controller;

import cat.cultura.backend.dtos.RouteDto;
import cat.cultura.backend.entity.Route;
import cat.cultura.backend.mappers.RouteMapper;
import cat.cultura.backend.service.RouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public class RouteController {
    @Autowired
    RouteService routeService;

    @Autowired
    RouteMapper routeMapper;

    @GetMapping("routes/{id}")
    public ResponseEntity<RouteDto> getRoute(@PathVariable Long id) {
        Route route = routeService.getRoute(id);
        return ResponseEntity.status(HttpStatus.OK).body(routeMapper.convertRouteToDto(route));
    }
}
