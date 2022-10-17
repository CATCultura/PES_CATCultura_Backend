package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Trophy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrophyJpaRepository extends JpaRepository<Trophy, Long> {
    Trophy findByName(String name);
}
