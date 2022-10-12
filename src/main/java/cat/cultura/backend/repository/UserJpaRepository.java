package cat.cultura.backend.repository;

import cat.cultura.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface UserJpaRepository extends JpaRepository<User, Long>{
    User findByName(String name);
}
