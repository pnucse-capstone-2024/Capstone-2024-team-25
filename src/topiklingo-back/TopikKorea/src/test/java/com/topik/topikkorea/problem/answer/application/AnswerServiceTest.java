package com.topik.topikkorea.problem.answer.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.problem.answer.AnswerFixture;
import com.topik.topikkorea.helper.problem.answer.AnswerUpdateRequestFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.question.QuestionFixture;
import com.topik.topikkorea.problem.application.answer.AnswerServiceImpl;
import com.topik.topikkorea.problem.application.answer.dto.request.AnswerCreateRequest;
import com.topik.topikkorea.problem.application.answer.dto.request.AnswerUpdateRequest;
import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.answer.repository.AnswerRepository;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
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
public class AnswerServiceTest {
    @Mock
    private AnswerRepository answerRepository;

    @InjectMocks
    private AnswerServiceImpl answerService;

    private Problem problem;

    private Question question;

    private Answer answer;

    private Answer answer2;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
        question = QuestionFixture.testIdQuestion(problem);
        answer = AnswerFixture.testAnswer(question);
        answer2 = AnswerFixture.testAnswer(question);
    }

    @Test
    @DisplayName("[success] Answer 저장 성공")
    public void createAnswer_Answer_저장_성공() {
        // given
        AnswerCreateRequest answerCreateRequest = AnswerCreateRequest.builder()
                .uuid(answer.getId())
                .AType(answer.getAType().name())
                .answer(answer.getAnswer())
                .build();

        AnswerCreateRequest answerCreateRequest2 = AnswerCreateRequest.builder()
                .uuid(answer2.getId())
                .AType(answer2.getAType().name())
                .answer(answer2.getAnswer())
                .build();

        // mocking
        when(answerRepository.saveAll(any(List.class))).thenReturn(List.of(answer, answer2));

        // when
        answerService.createAnswer(List.of(answerCreateRequest, answerCreateRequest2), question);

        // then
        verify(answerRepository).saveAll(any(List.class));
    }

    @Test
    @DisplayName("[success] Answer 업데이트 성공")
    public void updateAnswers_Answer_업데이트_성공() {
        // given
        AnswerUpdateRequest answerUpdateRequest =
                AnswerUpdateRequestFixture.testAnswerUpdateRequest(answer.getId());

        AnswerUpdateRequest answerUpdateRequest2 =
                AnswerUpdateRequestFixture.testAnswerUpdateRequest(answer2.getId());

        // mocking
        when(answerRepository.findAllByIds(List.of(answer.getId(), answer2.getId())))
                .thenReturn(Optional.of(List.of(answer, answer2)));
        when(answerRepository.saveAll(any(List.class))).thenReturn(List.of(answer, answer2));

        // when
        answerService.updateAnswers(List.of(answerUpdateRequest, answerUpdateRequest2));

        // then
        verify(answerRepository).findAllByIds(List.of(answer.getId(), answer2.getId()));
        verify(answerRepository).saveAll(any(List.class));
    }
}
