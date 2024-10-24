package com.topik.topikkorea.problem.question.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.question.QuestionFixture;
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
public class QuestionRepositoryTest {
    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ProblemRepository problemRepository;

    private Problem problem;

    private Question question;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
        problem = problemRepository.save(problem);

        question = QuestionFixture.testIdQuestion(problem);
    }

    @Test
    @Transactional
    @DisplayName("[success]문제 저장 성공")
    public void insertQuestion_Question_저장_성공() {
        // when
        Question savedQuestion = questionRepository.save(question);

        // then
        assertThat(savedQuestion.getId()).isNotNull();
    }

    @Test
    @Transactional
    @DisplayName("[success] Question 조회 성공")
    public void getQuestion_Question_조회_성공() {
        // given
        Question savedQuestion = questionRepository.save(question);

        // when
        Question findQuestion = questionRepository
                .findById(question.getId()).get();

        // then
        assertThat(findQuestion).isEqualTo(savedQuestion);
    }

    @Test
    @Transactional
    @DisplayName("[success] Problem으로 Question 조회 성공")
    public void getQuestionsByProblem_Problem으로_Question_조회_성공() {
        // given
        questionRepository.save(question);
        Question question2 = QuestionFixture.testIdQuestion(problem);
        questionRepository.save(question2);

        // when
        List<Question> findQuestions = questionRepository
                .getQuestionsByProblem(problem).get();

        // then
        assertThat(findQuestions.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    @DisplayName("[success] Question 아이디들로 Question 조회 성공")
    public void findAllByIds_Question_아이디들로_Question_조회_성공() {
        // given
        questionRepository.save(question);
        Question question2 = QuestionFixture.testIdQuestion(problem);
        questionRepository.save(question2);

        // when
        List<Question> findQuestions = questionRepository
                .findAllByIds(List.of(question.getId(), question2.getId())).get();

        // then
        assertThat(findQuestions.size()).isEqualTo(2);
    }
}
