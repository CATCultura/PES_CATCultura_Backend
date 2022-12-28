package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.User;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.exceptions.TagNotFoundException;
import cat.cultura.backend.exceptions.UserNotFoundException;
import cat.cultura.backend.repository.TagJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserTagService {

    private TagJpaRepository tagRepo;

    private UserJpaRepository userRepo;

    public UserTagService(TagJpaRepository tagRepo, UserJpaRepository userRepo) {
        this.tagRepo = tagRepo;
        this.userRepo = userRepo;
    }

    @Transactional(rollbackFor=Exception.class)
    public void addTags(Long userId, List<String> tagValues) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        for (String tag : tagValues) {
            Tag t = tagRepo.findByValue(tag).orElseThrow(TagNotFoundException::new);
            user.addTag(t);
        }
    }

    @Transactional(rollbackFor=Exception.class)
    public void removeTags(Long userId, List<String> tagValues) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        for (String tag : tagValues) {
            Tag t = tagRepo.findByValue(tag).orElseThrow(TagNotFoundException::new);
            user.removeTag(t);
        }
    }

    public List<Tag> getTags(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(UserNotFoundException::new);
        List<Tag> tagList = new ArrayList<>();
        tagList.addAll(user.getTagsAmbits());
        tagList.addAll(user.getTagsCateg());
        tagList.addAll(user.getTagsAltresCateg());
        return tagList;
    }


}
