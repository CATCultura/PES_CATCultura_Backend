package cat.cultura.backend.dto;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.dtos.UserDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runners.Suite;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventDtoTest {
    private ModelMapper modelMapper = new ModelMapper();

    @Test
    void whenConvertEventEntityToEventDto_thenCorrect() {
        Event event = new Event();
        event.setEmail("barmanolo@hotmail.com");
        event.setURL("coctelesmanolo.com");

        EventDto eventDto = modelMapper.map(event, EventDto.class);
        assertEquals(event.getId(), eventDto.getId());
        assertEquals(event.getEmail(), eventDto.getEmail());
        assertEquals(event.getURL(), eventDto.getURL());
    }

    @Test
    void whenConvertEventDtoToEventEntity_thenCorrect() {
        EventDto eventDto = new EventDto();
        eventDto.setEmail("davidguetta@business.com");
        eventDto.setURL("davidguetta.com");

        Event event = modelMapper.map(eventDto, Event.class);
        assertEquals(eventDto.getId(), event.getId());
        assertEquals(eventDto.getEmail(), event.getEmail());
        assertEquals(eventDto.getURL(), event.getURL());
    }
}
