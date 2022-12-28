package cat.cultura.backend.repository;

import cat.cultura.backend.entity.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagJpaRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findByValue(String value);

    boolean existsByValue(String value);



}
