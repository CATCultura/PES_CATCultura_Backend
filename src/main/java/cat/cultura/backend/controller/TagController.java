package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TagController {

    public static final String AMBITS = "ambits";
    public static final String CATEGORIES = "categories";
    public static final String ALTRES_CATEGORIES = "altresCategories";
    @Autowired
    private EventService eventService;

    @GetMapping("/tags")
    public Map<String, Set<String>> getAllTags() {
        List<Event> eventList = eventService.getEvents();
        HashMap<String, Set<String>> tags = new HashMap<>();

        tags.put(AMBITS, new HashSet<>());
        tags.put(CATEGORIES, new HashSet<>());
        tags.put(ALTRES_CATEGORIES, new HashSet<>());

        for (Event e : eventList) {
            for (String tag : e.getTagsAmbits())
                tags.get(AMBITS).add(tag);

            for (String tag : e.getTagsCateg())
                tags.get(CATEGORIES).add(tag);

            for (String tag : e.getTagsAltresCateg())
                tags.get(ALTRES_CATEGORIES).add(tag);
        }
        return tags;
    }

}
