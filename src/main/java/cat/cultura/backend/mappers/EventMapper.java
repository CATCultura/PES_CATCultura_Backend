package cat.cultura.backend.mappers;

import cat.cultura.backend.dtos.EventDto;
import cat.cultura.backend.entity.Event;
import cat.cultura.backend.entity.Organizer;
import cat.cultura.backend.entity.tag.*;
import cat.cultura.backend.exceptions.MissingRequiredParametersException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class EventMapper {

    private final ModelMapper modelMapper;
    public EventMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Event convertEventDtoToEntity(EventDto eventDto) {
        if (eventDto.getDenominacio() == null) throw new MissingRequiredParametersException();
        if (eventDto.getDataInici() == null) throw new MissingRequiredParametersException();
        if (eventDto.getUbicacio() == null) throw new MissingRequiredParametersException();

        Event result = modelMapper.map(eventDto, Event.class);
        Organizer org = extractOrganizerInfo(eventDto);
        List<Tag> tagAmbits = extractTags(eventDto, Type.AMBITS);
        result.setTagsAmbits(tagAmbits);
        List<Tag> tagCategories = extractTags(eventDto, Type.CATEGORIES);
        result.setTagsCateg(tagCategories);
        List<Tag> tagAltresCategories = extractTags(eventDto, Type.ALTRES_CATEGORIES);
        result.setTagsAltresCateg(tagAltresCategories);
        result.setOrganizer(org);
        return result;
    }

    public EventDto convertEventToDto(Event event) {
        EventDto result = modelMapper.map(event, EventDto.class);
        result.setTagsAmbits(extractTags(event,Type.AMBITS));
        result.setTagsCateg(extractTags(event,Type.CATEGORIES));
        result.setTagsAltresCateg(extractTags(event,Type.ALTRES_CATEGORIES));
        populateOrganizerInfo(event,result);
        return result;
    }

    private List<Tag> extractTags(EventDto eventDto, Type t) {
        List<Tag> result = new ArrayList<>();
        switch (t) {
            case AMBITS -> {
                for (String s : eventDto.getTagsAmbits()) {
                    result.add(new TagAmbits(s));
                }
                return result;
            }
            case CATEGORIES -> {
                for (String s : eventDto.getTagsCateg()) {
                    result.add(new TagCategories(s));
                }
                return result;
            }
            case ALTRES_CATEGORIES -> {
                for (String s : eventDto.getTagsAltresCateg()) {
                    result.add(new TagAltresCategories(s));
                }
                return result;
            }

        }
        return result;
    }

    private List<String> extractTags(Event event, Type t) {
        List<String> result = new ArrayList<>();
        switch (t) {
            case AMBITS -> {
                for (Tag s : event.getTagsAmbits()) {
                    result.add(s.getValue());
                }
                return result;
            }
            case CATEGORIES -> {
                for (Tag s : event.getTagsCateg()) {
                    result.add(s.getValue());
                }
                return result;
            }
            case ALTRES_CATEGORIES -> {
                for (Tag s : event.getTagsAltresCateg()) {
                    result.add(s.getValue());
                }
                return result;
            }

        }
        return result;
    }


    private Organizer extractOrganizerInfo(EventDto eventDto) {
        String nomOrganitzador = eventDto.getNomOrganitzador();
        String urlOrganitzador = eventDto.getUrlOrganitzador();
        String telefonOrganitzador = eventDto.getTelefonOrganitzador();
        String emailOrganitzador = eventDto.getEmailOrganitzador();
        if (nomOrganitzador != null && (urlOrganitzador != null || telefonOrganitzador != null || emailOrganitzador != null)) {
            Organizer org;
            org = new Organizer(nomOrganitzador);
            org.setUrl(urlOrganitzador);
            org.setTelefon(telefonOrganitzador);
            org.setEmail(emailOrganitzador);
            return org;
        }
        return null;
    }

    private void populateOrganizerInfo(Event source, EventDto target) {
        if (source.getOrganizer() != null) {
            target.setIdOrganitzador(source.getOrganizer().getId());
            target.setNomOrganitzador(source.getOrganizer().getUsername());
            target.setEmailOrganitzador(source.getOrganizer().getEmail());
            target.setUrlOrganitzador(source.getOrganizer().getUrl());
        }
    }


}
