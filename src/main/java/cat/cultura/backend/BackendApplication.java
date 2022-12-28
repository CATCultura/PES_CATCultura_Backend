package cat.cultura.backend;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication()
@RestController
public class BackendApplication  implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		PropertyMap<EventDto, Event> propertyMap = new PropertyMap<EventDto, Event>() {
			@Override
			protected void configure() {
				skip(destination.getTagsAmbits());
				skip(destination.getTagsAltresCateg());
				skip(destination.getTagsCateg());
			}
		};
		modelMapper.addMappings(propertyMap);
		return modelMapper;
	}

}
