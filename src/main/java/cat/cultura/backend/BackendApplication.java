package cat.cultura.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class })
@RestController
public class BackendApplication implements CommandLineRunner {

	@Autowired
	private EventDao esdvDao;
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@PostMapping("/insert")
	public int insertData(@RequestBody List<Event> events) {
		return 1;
	}

	@Override
	public void run(String... args) throws Exception {
		Event esdv = getEsdeveniment();
		esdvDao.createEvent(esdv);
	}

	private Event getEsdeveniment() {
		Event esdv = new Event();
		esdv.setCodi(3333);
		return esdv;
	}

}
