package com.heydocter.app.mapper;

import com.heydoctor.app.HeydocterApplication;
import com.heydoctor.app.domain.dto.QnaDTO;
import com.heydoctor.app.domain.dto.QuestionDTO;
import com.heydoctor.app.domain.enums.DepartmentType;
import com.heydoctor.app.domain.enums.QuestionType;
import com.heydoctor.app.domain.vo.QuestionVO;
import com.heydoctor.app.domain.vo.UserVO;
import com.heydoctor.app.mapper.AdminMapper;
import com.heydoctor.app.mapper.QuestionMapper;
import com.heydoctor.app.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = HeydocterApplication.class)
@Slf4j
public class AdminMapperTests {

    @Autowired
    private AdminMapper adminMapper;

    @Test
    public void selectListTest() {
        AssertionsForClassTypes.assertThat(adminMapper.selectLists(0).size()).isEqualTo(10);
    }

    @Test
    public void deleteTest() {
        adminMapper.delete(11L);
        assertThat(adminMapper.select(11L)).isNull();
    }

    @Test
    public void selectOneTest() {
        assertThat(adminMapper.select(4L)).isNotNull();
    }
}
