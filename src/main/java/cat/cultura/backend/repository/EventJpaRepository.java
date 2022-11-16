package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventJpaRepository extends JpaRepository<Event, Long> {

    Optional<Event> findByCodi(Long codi);

    @Query("select m from Event m where " +
            "(?1 is null or m.id = ?1) " )
    Page<Event> getByQuery(Long id, final Pageable pageable);

    List<Event> findByDenominacioLikeIgnoreCaseAllIgnoreCase(String denominacio);

    @Query("select m from Event m where " +
            "(m.dataInici <= ?1) and" +
            "(m.dataInici >= ?2) and" +
            "((m.lat <= ?3 and m.lon <= ?4) or" +
            "(m.lat >= ?5 and m.lon >= ?6) or" +
            "(m.lat <= ?7 and m.lon <= ?8) or" +
            "(m.lat >= ?9 and m.lon >= ?10))")
    List<Event> getEventsByDayAndLocation(String day1, String day2, double lat, double lon, double lat1,
                                          double lon1, double lat2, double lon2, double lat3, double lon3);
}
