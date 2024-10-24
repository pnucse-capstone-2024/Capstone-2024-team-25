package com.topik.topikkorea.write.question;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.write.problem.WriteProblemFixture;
import com.topik.topikkorea.helper.write.question.WriteQuestionFixture;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.problem.repository.WriteProblemRepository;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import com.topik.topikkorea.write.domain.question.repository.WriteQuestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WriteQuestionRepositoryTest {
    @Autowired
    private WriteProblemRepository writeProblemRepository;

    @Autowired
    private WriteQuestionRepository writeQuestionRepository;

    private WriteProblem writeProblem;

    private WriteQuestion writeQuestion;

    @BeforeEach
    public void init() {
        writeProblem = WriteProblemFixture.testWriteProblem(WriteProblemType.WRITING_2_PROBLEM_TYPE_1);
        writeProblemRepository.save(writeProblem);

        writeQuestion = WriteQuestionFixture.testWriteQuestion(writeProblem);
    }

    @Test
    @DisplayName("[success] WriteQuestion를 저장에 성공")
    public void save_write_question() {
        writeQuestionRepository.save(writeQuestion);

        assertThat(writeQuestion.getId()).isNotNull();
    }
}
