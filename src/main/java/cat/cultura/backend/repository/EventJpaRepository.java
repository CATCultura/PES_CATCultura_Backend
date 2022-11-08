package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByCodi(Long codi);

    @Query("select m from Event m where " +
            "(?1 is null or m.id = ?1) " )
    Page<Event> getByQuery(Long id, final Pageable pageable);

}
