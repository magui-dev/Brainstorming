package com.brainstorming.brainstorming_platform.domain.idea.service;

import com.brainstorming.brainstorming_platform.domain.idea.entity.Idea;
import com.brainstorming.brainstorming_platform.domain.idea.repository.IdeaRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
class IdeaServiceTest {

    @Autowired
    private IdeaRepository ideaRepository;
    @Autowired
    private IdeaService ideaService;

    @Test
    void save() {
        //given
        Idea idea = new Idea(
                null,
                1L,
                "testTitle",
                "testContent",
                "testPurpose"
        );
        //when
        Idea savedIdea = ideaService.save(idea);
        //then
        assertThat(savedIdea.getIdeaId()).isNotNull();
        assertThat(savedIdea.getUserId()).isEqualTo(1L);
        assertThat(savedIdea.getTitle()).isEqualTo("testTitle");
        assertThat(savedIdea.getContent()).isEqualTo("testContent");
        assertThat(savedIdea.getPurpose()).isEqualTo("testPurpose");

    }

    @Test
    @DisplayName("아이디어Id 로 아이디어 정보 검색 ")
    void findById() {
        //given
        Idea idea = new Idea(null, 2L, "testTitle2", "testContent2", "testPurpose2");
        ideaService.save(idea);
        //when

        Idea findIdea = ideaService.findById(idea.getIdeaId());

        //then
        assertThat(findIdea.getIdeaId()).isNotNull();
        assertThat(findIdea.getUserId()).isEqualTo(2L);
        assertThat(findIdea.getTitle()).isEqualTo("testTitle2");
        assertThat(findIdea.getContent()).isEqualTo("testContent2");
        assertThat(findIdea.getPurpose()).isEqualTo("testPurpose2");
    }

    @Test
    @DisplayName("사용자 Id로 사용자의 전체 아이디어 검색")
    void findByUserId() {
        //given
        Idea idea1 = new Idea(null, 1L, "testTitle", "testContent", "testPurpose");
        Idea idea2 = new Idea(null, 2L, "testTitle2", "testContent2", "testPurpose2");
        Idea idea3 = new Idea(null, 1L, "testTitle3", "testContent3", "testPurpose3");
        ideaService.save(idea1);
        ideaService.save(idea2);
        ideaService.save(idea3);

        //when
        List<Idea> ideas = ideaService.findByUserId(1L);

        //then
        assertThat(ideas).hasSize(2); //아이디어가 2개인가
        assertThat(ideas).extracting("userId").containsOnly(1L); // userId= 1L만 출력했는가
        assertThat(ideas).extracting("title")
                .containsExactlyInAnyOrder("testTitle", "testTitle3"); //title은 2개의 testTitle,testTitle3를 가지고있는가
    }

    @Test
    @DisplayName("아이디어 삭제")
    void delete() {
        //given
        Idea idea = new Idea(null, 1L, "testTitle", "testContent", "testPurpose");
        ideaService.save(idea);
        Long ideaId = idea.getIdeaId();

        //when
        ideaService.delete(ideaId);

        //then
        assertThat(ideaRepository.existsById(ideaId)).isFalse();


    }

    @Test
    void countByUserId() {
        //given
        Idea idea1 = new Idea(null, 1L, "testTitle", "testContent", "testPurpose");
        Idea idea2 = new Idea(null, 2L, "testTitle2", "testContent2", "testPurpose2");
        Idea idea3 = new Idea(null, 1L, "testTitle3", "testContent3", "testPurpose3");
        Idea idea4 = new Idea(null, 2L, "testTitle4", "testContent4", "testPurpose4");
        Idea idea5 = new Idea(null, 1L, "testTitle5", "testContent5", "testPurpose5");

        ideaService.save(idea1);
        ideaService.save(idea2);
        ideaService.save(idea3);
        ideaService.save(idea4);
        ideaService.save(idea5);

        //when
        long count1 = ideaService.countByUserId(1L);
        long count2 = ideaService.countByUserId(2L);

        //then
        assertThat(count1).isEqualTo(3);
        assertThat(count2).isEqualTo(2);
    }
}