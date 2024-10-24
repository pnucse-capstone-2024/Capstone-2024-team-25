package com.topik.topikkorea.write.problem;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.write.problem.WriteProblemFixture;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.problem.repository.WriteProblemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class WriteProblemRepositoryTest {

    @Autowired
    private WriteProblemRepository writeProblemRepository;

    private WriteProblem writeProblem;

    @BeforeEach
    public void init() {
        writeProblem = WriteProblemFixture.testWriteProblem(WriteProblemType.WRITING_2_PROBLEM_TYPE_1);
    }

    @Test
    @DisplayName("[success] WriteProblem를 저장에 성공")
    public void save_write_problem() {
        writeProblemRepository.save(writeProblem);

        assertThat(writeProblem.getId()).isNotNull();
    }
}
