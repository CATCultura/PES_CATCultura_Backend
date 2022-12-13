package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteJpaRepository extends JpaRepository<Route, Long> {
}
