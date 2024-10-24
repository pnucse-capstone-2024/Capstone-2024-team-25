package com.topik.topikkorea.problem.answer.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.problem.answer.AnswerFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.question.QuestionFixture;
import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.answer.repository.AnswerRepository;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.domain.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DataJpaTest
public class AnswerRepositoryTest {
    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ProblemRepository problemRepository;

    private Answer answer;

    private Question question;

    private Problem problem;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
        problem = problemRepository.save(problem);

        question = QuestionFixture.testIdQuestion(problem);
        question = questionRepository.save(question);

        answer = AnswerFixture.testAnswer(question);
    }

    @Test
    @Transactional
    @DisplayName("[success] answer 저장 성공")
    public void createAnswer_answer_저장_성공() {
        // when
        Answer savedAnswer = answerRepository.save(answer);

        // then
        assertThat(savedAnswer.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] answer 조회 성공")
    public void getAnswer_answer_조회_성공() {
        // given
        Answer savedAnswer = answerRepository.save(answer);

        // when
        Answer findAnswer = answerRepository
                .findById(answer.getId()).get();

        // then
        assertThat(findAnswer.getId()).isEqualTo(savedAnswer.getId());
    }

    @Test
    @Transactional
    @DisplayName("[success] Answer 아이디들로 Answer 조회 성공")
    public void updateAnswer_answer_업데이트_성공() {
        // given
        Answer savedAnswer = answerRepository.save(answer);
        Answer answer2 = AnswerFixture.testAnswer(question);
        Answer savedAnswer2 = answerRepository.save(answer2);

        // when
        List<Answer> findAnswers = answerRepository
                .findAllByIds(List.of(savedAnswer.getId(), savedAnswer2.getId())).get();

        // then
        assertThat(findAnswers.size()).isEqualTo(2);
    }
}
