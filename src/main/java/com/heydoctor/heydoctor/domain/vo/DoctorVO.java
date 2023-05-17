package com.heydoctor.heydoctor.domain.vo;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class DoctorVO {
  private Long userId;
  private Long hospitalId;
  private String certificationId;
  private Boolean isBookable;
  private String doctorIntroduction;
}
