package cat.cultura.backend.repository;

import cat.cultura.backend.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>{

    Optional<User> findByUsername(String username);

    @Query("select m from User m where " +
            "(?1 is null or m.id = ?1) " +
            "and (?2 is null or upper(m.username) like concat('%', upper(?2), '%'))" +
            "and (?3 is null or upper(m.nameAndSurname) like concat('%', upper(?3), '%'))" )
    Page<User> getUsersByQuery(Long id, String username, String nameAndSurname, final Pageable pageable);

}
