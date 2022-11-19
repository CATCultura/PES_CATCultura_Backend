package cat.cultura.backend.controller;

import cat.cultura.backend.entity.Event;
import cat.cultura.backend.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TagController {

    @Autowired
    private EventService eventService;

    @GetMapping("/tags")
    public Map<String, Set<String>> getAllTags() {
        List<Event> eventList = eventService.getEvents();
        HashMap<String, Set<String>> tags = new HashMap<>();

        String ambits = "ambits";
        String categories = "categories";
        String altresCategories = "altresCategories";

        tags.put(ambits, new HashSet<>());
        tags.put(categories, new HashSet<>());
        tags.put(altresCategories, new HashSet<>());

        for (Event e : eventList) {
            for (String tag : e.getTagsAmbits())
                tags.get(ambits).add(tag);

            for (String tag : e.getTagsCateg())
                tags.get(categories).add(tag);

            for (String tag : e.getTagsAltresCateg())
                tags.get(altresCategories).add(tag);
        }
        return tags;
    }

}
