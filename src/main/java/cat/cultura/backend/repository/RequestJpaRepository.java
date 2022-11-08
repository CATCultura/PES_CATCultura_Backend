package cat.cultura.backend.repository;

import cat.cultura.backend.entity.Request;
import cat.cultura.backend.utils.RequestId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestJpaRepository extends JpaRepository<Request, RequestId> {

}
