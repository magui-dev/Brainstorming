package com.brainstorming.brainstorming_platform.domain.inquiry.service;

import com.brainstorming.brainstorming_platform.domain.inquiry.entity.Inquiry;
import com.brainstorming.brainstorming_platform.domain.inquiry.entity.InquiryStatus;
import com.brainstorming.brainstorming_platform.domain.inquiry.repository.InquiryRepository;
import jakarta.persistence.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class InquiryServiceTest {

    @Autowired
    private InquiryService inquiryService;
    @Autowired
    private InquiryRepository inquiryRepository;

    @Test
    @DisplayName("문의내용 저장")
    void save() {
        //given
        Inquiry inquiry = new Inquiry(
                null,
                1L,
                "title",
                "content",
                InquiryStatus.PENDING,
                "reply"
        );

        //when
        Inquiry saveInquiry = inquiryService.save(inquiry);

        //then
        assertThat(saveInquiry.getInquiryId()).isNotNull();
        assertThat(saveInquiry.getUserId()).isEqualTo(1L);
        assertThat(saveInquiry.getTitle()).isEqualTo("title");
        assertThat(saveInquiry.getContent()).isEqualTo("content");
        assertThat(saveInquiry.getStatus()).isEqualTo(InquiryStatus.PENDING);
        assertThat(saveInquiry.getReply()).isEqualTo("reply");
    }

    @Test
    @DisplayName("특정 ID의 문의글 1개 조회 ")
    void findById() {
        //given
        Inquiry inquiry1 = new Inquiry(null, 1L, "title1", "content1", InquiryStatus.PENDING, "reply1");
        Inquiry saveInquiry = inquiryService.save(inquiry1);

        //when
        Inquiry inquiry = inquiryService.findById(saveInquiry.getInquiryId());

        //then
        assertThat(inquiry.getInquiryId()).isNotNull();
        assertThat(inquiry.getUserId()).isEqualTo(1L);
        assertThat(inquiry.getTitle()).isEqualTo("title1");
        assertThat(inquiry.getContent()).isEqualTo("content1");
        assertThat(inquiry.getStatus()).isEqualTo(InquiryStatus.PENDING);
        assertThat(inquiry.getReply()).isEqualTo("reply1");


    }

    @Test
    @DisplayName("userID로 해당 사용자의 모든 문의글 조회")
    void findByUserId() {
        //given
        Inquiry inquiry1 = new Inquiry(null, 1L, "title1", "content1", InquiryStatus.PENDING, "reply1");
        Inquiry inquiry2 = new Inquiry(null, 2L, "title2", "content2", InquiryStatus.PENDING, "reply2");
        Inquiry inquiry3 = new Inquiry(null, 1L, "title3", "content3", InquiryStatus.ANSWERED, "reply3");
        inquiryService.save(inquiry1);
        inquiryService.save(inquiry2);
        inquiryService.save(inquiry3);

        //when
        List<Inquiry> userInquiries = inquiryService.findByUserId(1L);

        //then
        assertThat(userInquiries).hasSize(2);
        assertThat(userInquiries).extracting("userId").containsOnly(1L);
        assertThat(userInquiries).extracting("title")
                .containsExactlyInAnyOrder("title1", "title3");
    }

    @Test
    void delete() {
        //given
        Inquiry inquiry1 = new Inquiry(null, 1L, "title1", "content1", InquiryStatus.PENDING, "reply1");
        Inquiry saveInquiry = inquiryService.save(inquiry1);
        Long inquiryId = saveInquiry.getInquiryId(); //저장된 ID 먼저 가져오기

        //when
        inquiryService.delete(inquiryId);

        //then
        assertThatThrownBy(() -> inquiryService.findById(inquiryId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("저장된 문의사항이 없습니다.");
    }
}