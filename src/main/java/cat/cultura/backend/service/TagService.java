package cat.cultura.backend.service;

import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.TagNotFoundException;
import cat.cultura.backend.repository.TagJpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagJpaRepository tagRepo;

    public TagService(TagJpaRepository repo) {
        this.tagRepo = repo;
    }

    public List<Tag> getAllTags() {
        return tagRepo.findAll();
    }

    public Tag getTagById(Long id) {
        return tagRepo.findById(id).orElseThrow(TagNotFoundException::new);
    }

    public Tag getTagByName(String name) {
        return tagRepo.findByValue(name).orElseThrow(TagNotFoundException::new);
    }
}
