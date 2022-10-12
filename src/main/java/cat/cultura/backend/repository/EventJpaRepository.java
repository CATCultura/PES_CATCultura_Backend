package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {
    //Event findByCodi(Long codi);
}
