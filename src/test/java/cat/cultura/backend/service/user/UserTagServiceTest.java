package cat.cultura.backend.service.user;

import cat.cultura.backend.entity.User;
import cat.cultura.backend.entity.tag.Tag;
import cat.cultura.backend.entity.tag.TagAmbits;
import cat.cultura.backend.entity.tag.TagCategories;
import cat.cultura.backend.repository.TagJpaRepository;
import cat.cultura.backend.repository.UserJpaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class UserTagServiceTest {

    @Autowired
    private UserTagService userTagService;
    @MockBean
    private UserJpaRepository userRepo;

    @MockBean
    private TagJpaRepository tagRepo;

    @Test
    void addOneTagWorks() {
        User u = new User("Joanot");
        u.setId(1L);
        given(userRepo.findById(1L)).willReturn(Optional.of(u));

        Tag t = new TagAmbits("arts-visuals");
        t.setId(1L);
        given(tagRepo.findByValue(t.getValue())).willReturn(Optional.of(t));

        List<String> tagList = new ArrayList<>();
        tagList.add(t.getValue());

        userTagService.addTags(1L,tagList);

        Assertions.assertTrue(u.getTagsAmbits().contains(t));

    }

    @Test
    void addSeveralTagsWorks() {
        User u = new User("Joanot");
        u.setId(1L);
        given(userRepo.findById(1L)).willReturn(Optional.of(u));

        Tag t1 = new TagAmbits("arts-visuals");
        t1.setId(1L);
        Tag t2 = new TagCategories("nadal");
        t2.setId(2L);

        given(tagRepo.findByValue(t1.getValue())).willReturn(Optional.of(t1));
        given(tagRepo.findByValue(t2.getValue())).willReturn(Optional.of(t2));

        List<String> tagList = new ArrayList<>();
        tagList.add(t1.getValue());
        tagList.add(t2.getValue());

        userTagService.addTags(1L,tagList);

        Assertions.assertTrue(u.getTagsAmbits().contains(t1) && u.getTagsCateg().contains(t2));

    }

    @Test
    void removeOneTagWorks() {
        User u = new User("Joanot");
        u.setId(1L);
        given(userRepo.findById(1L)).willReturn(Optional.of(u));

        Tag t = new TagAmbits("arts-visuals");
        t.setId(1L);
        given(tagRepo.findByValue(t.getValue())).willReturn(Optional.of(t));

        u.addTag(t);
        List<String> tagList = new ArrayList<>();
        tagList.add(t.getValue());

        userTagService.removeTags(1L,tagList);

        Assertions.assertFalse(u.getTagsAmbits().contains(t));
    }

    @Test
    void removingSeveralTagsWorks() {
        User u = new User("Joanot");
        u.setId(1L);
        given(userRepo.findById(1L)).willReturn(Optional.of(u));

        Tag t1 = new TagAmbits("arts-visuals");
        t1.setId(1L);
        Tag t2 = new TagCategories("nadal");
        t2.setId(2L);
        u.addTag(t1);
        u.addTag(t2);

        given(tagRepo.findByValue(t1.getValue())).willReturn(Optional.of(t1));
        given(tagRepo.findByValue(t2.getValue())).willReturn(Optional.of(t2));

        List<String> tagList = new ArrayList<>();
        tagList.add(t1.getValue());
        tagList.add(t2.getValue());

        userTagService.removeTags(1L,tagList);

        Assertions.assertFalse(u.getTagsAmbits().contains(t1) || u.getTagsCateg().contains(t2));

    }

    @Test
    void getTags() {
        User u = new User("Joanot");
        u.setId(1L);
        given(userRepo.findById(1L)).willReturn(Optional.of(u));

        Tag t1 = new TagAmbits("arts-visuals");
        t1.setId(1L);
        Tag t2 = new TagCategories("nadal");
        t2.setId(2L);
        u.addTag(t1);
        u.addTag(t2);

        given(tagRepo.findByValue(t1.getValue())).willReturn(Optional.of(t1));
        given(tagRepo.findByValue(t2.getValue())).willReturn(Optional.of(t2));

        List<Tag> tagList = new ArrayList<>();
        tagList.add(t1);
        tagList.add(t2);



        Assertions.assertEquals(tagList,userTagService.getTags(1L));
    }
}