package com.heydoctor.heydoctor.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class AnswerVO {
   private Long answerId;
   private Long userId;
   private Long questionId;
   private String answerTitle;
   private String answerContent;
   private String answerRegisterDateTime;
   private Boolean answerIsPublic;
   private Long answerLikeCount;
   private Boolean answerIsAdopt;
}
