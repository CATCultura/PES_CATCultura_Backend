package cat.cultura.backend.service;

import cat.cultura.backend.entity.Route;
import cat.cultura.backend.exceptions.RouteNotFoundException;
import cat.cultura.backend.repository.RouteJpaRepository;

public class RouteService {
    private final RouteJpaRepository routeRepo;

    public RouteService(RouteJpaRepository routeRepo) {
        this.routeRepo = routeRepo;
    }

    public Route getRoute(Long routeId) {
        return routeRepo.findById(routeId).orElseThrow(RouteNotFoundException::new);
    }
}
