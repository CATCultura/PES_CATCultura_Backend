package cat.cultura.backend;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.factories.RepoFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.util.List;

@SpringBootApplication()
@RestController
public class BackendApplication {

	@Autowired
	private RepoFactory repoFactory;

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostConstruct
	public void initialize() {
		repoFactory.initialize();
	}



}
