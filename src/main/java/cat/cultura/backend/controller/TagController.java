package cat.cultura.backend.controller;

import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.entity.tag.Type;
import cat.cultura.backend.service.EventService;
import cat.cultura.backend.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TagController {


    @Autowired
    private EventService eventService;

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public Map<String, Set<String>> getAllTags() {
        List<Tag> tagList = tagService.getAllTags();
        HashMap<String, Set<String>> tags = new HashMap<>();

        tags.put(Type.AMBITS.toString(), new HashSet<>());
        tags.put(Type.CATEGORIES.toString(), new HashSet<>());
        tags.put(Type.ALTRES_CATEGORIES.toString(), new HashSet<>());

        for (Tag t : tagList) {
            tags.get(t.getType().toString()).add(t.getValue());
        }
        return tags;
    }

}
