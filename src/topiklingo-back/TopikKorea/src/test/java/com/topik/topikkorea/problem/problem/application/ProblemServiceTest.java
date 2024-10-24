package com.topik.topikkorea.problem.problem.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemRequestFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemUpdateRequestFixture;
import com.topik.topikkorea.problem.application.problem.ProblemServiceImpl;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemUpdateRequest;
import com.topik.topikkorea.problem.application.question.QuestionService;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
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
public class ProblemServiceTest {
    @Mock
    private ProblemRepository problemRepository;

    @Mock
    private QuestionService questionService;

    @InjectMocks
    private ProblemServiceImpl problemService;

    Problem problem;

    Problem problem2;

    @BeforeEach
    public void init() {
        problem = ProblemFixture.testIdProblem();
        problem2 = ProblemFixture.testIdProblem();
    }

    @Test
    @DisplayName("[success] 다중 문제 저장 성공")
    public void createManyProblems_다중_문제_저장() {
        // given
        ProblemRequest problemRequest =
                ProblemRequestFixture.testProblemRequest(problem);

        ProblemRequest problemRequest2 =
                ProblemRequestFixture.testProblemRequest(problem2);

        // mocking
        when(problemRepository.saveAll(any(List.class))).thenReturn(List.of(problem, problem2));
        doNothing().when(questionService).createQuestion(any(), any(Problem.class));

        // when
        List<Problem> problems = problemService.createManyProblems(List.of(problemRequest, problemRequest2));

        // then
        assertThat(problems.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("[success] 문제 업데이트 성공")
    public void updateProblem_문제_업데이트() {
        // given
        ProblemUpdateRequest problemRequest =
                ProblemUpdateRequestFixture.testProblemUpdateRequest(problem.getId());

        ProblemUpdateRequest problemRequest2 =
                ProblemUpdateRequestFixture.testProblemUpdateRequest(problem2.getId());

        // mocking
        doNothing().when(questionService).updateQuestions(any(List.class));
        when(problemRepository.findAllByIds(any(List.class))).thenReturn(Optional.of(List.of(problem, problem2)));
        when(problemRepository.saveAll(any(List.class))).thenReturn(List.of(problem, problem2));

        // when
        problemService.updateProblems(List.of(problemRequest, problemRequest2));

        // then
        verify(questionService, times(2)).updateQuestions(any(List.class));
        verify(problemRepository).findAllByIds(any(List.class));
        verify(problemRepository).saveAll(any(List.class));
    }
}
