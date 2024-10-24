package com.topik.topikkorea.problem.question.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.question.QuestionFixture;
import com.topik.topikkorea.helper.problem.question.QuestionUpdateRequestFixture;
import com.topik.topikkorea.problem.application.answer.AnswerService;
import com.topik.topikkorea.problem.application.question.QuestionServiceImpl;
import com.topik.topikkorea.problem.application.question.dto.request.QuestionCreateRequest;
import com.topik.topikkorea.problem.application.question.dto.request.QuestionUpdateRequest;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.domain.question.repository.QuestionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class QuestionServiceTest {
    @Mock
    QuestionRepository questionRepository;

    @Mock
    AnswerService answerService;

    @InjectMocks
    QuestionServiceImpl questionService;

    private Problem problem;

    private Question question;

    private Question question2;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
        question = QuestionFixture.testIdQuestion(problem);
        question2 = QuestionFixture.testIdQuestion(problem);
    }

    @Test
    @Transactional
    @DisplayName("[success] Question 저장 성공")
    public void createQuestion_Question_저장_성공() {
        // given
        QuestionCreateRequest questionCreateRequest = QuestionCreateRequest.builder()
                .uuid(question.getId())
                .QEType(question.getQEType().name())
                .questionProblem(question.getQuestionProblem())
                .score(question.getScore())
                .rightAnswer(question.getRightAnswer())
                .explain(question.getExplain())
                .example(question.getExample())
                .answers(List.of())
                .build();

        QuestionCreateRequest questionCreateRequest2 = QuestionCreateRequest.builder()
                .uuid(question2.getId())
                .QEType(question2.getQEType().name())
                .questionProblem(question2.getQuestionProblem())
                .score(question2.getScore())
                .rightAnswer(question2.getRightAnswer())
                .explain(question2.getExplain())
                .example(question2.getExample())
                .answers(List.of())
                .build();

        // mocking
        when(questionRepository.saveAll(any(List.class))).thenReturn(List.of(question, question2));
        doNothing().when(answerService).createAnswer(any(List.class), any(Question.class));

        // when
        questionService.createQuestion(List.of(questionCreateRequest, questionCreateRequest2), problem);

        // then
        verify(questionRepository).saveAll(any(List.class));
        verify(answerService, times(2)).createAnswer(any(List.class), any(Question.class));
    }

    @Test
    @Transactional
    @DisplayName("[success] Question 업데이트 성공")
    public void updateQuestions_Question_업데이트_성공() {
        // given
        QuestionUpdateRequest questionUpdateRequest =
                QuestionUpdateRequestFixture.testQuestionUpdateRequest(question.getId());

        QuestionUpdateRequest questionUpdateRequest2 =
                QuestionUpdateRequestFixture.testQuestionUpdateRequest(question2.getId());

        // mocking
        doNothing().when(answerService).updateAnswers(any(List.class));
        when(questionRepository.findAllByIds(List.of(question.getId(), question2.getId())))
                .thenReturn(Optional.of(List.of(question, question2)));
        when(questionRepository.saveAll(any(List.class))).thenReturn(List.of(question, question2));

        // when
        questionService.updateQuestions(List.of(questionUpdateRequest, questionUpdateRequest2));

        // then
        verify(answerService, times(2)).updateAnswers(any(List.class));
        verify(questionRepository).findAllByIds(List.of(question.getId(), question2.getId()));
        verify(questionRepository).saveAll(any(List.class));
    }
}
