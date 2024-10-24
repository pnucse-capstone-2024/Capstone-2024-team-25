package com.topik.topikkorea.problem.application.problem;

import com.topik.topikkorea.problem.application.answer.dto.request.RandomProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemUpdateRequest;
import com.topik.topikkorea.problem.application.problem.dto.response.ProblemResponse;
import com.topik.topikkorea.problem.application.question.QuestionService;
import com.topik.topikkorea.problem.application.question.dto.response.QuestionResponse;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
import com.topik.topikkorea.problem.domain.question.Question;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemServiceImpl implements ProblemService {
    private final ProblemRepository problemRepository;
    private final QuestionService questionService;

    @Override
    @Transactional
    public void createProblem(final ProblemRequest request) {
        Problem problem = problemRepository.save(
                Problem.builder()
                        .uuid(request.uuid())
                        .PType(request.PType())
                        .problem(request.problem())
                        .EType(request.EType())
                        .example(request.example())
                        .build()
        );

        questionService.createQuestion(request.questions(), problem);
    }

    @Override
    @Transactional
    public List<Problem> createManyProblems(final List<ProblemRequest> problemRequests) {
        List<Problem> problems = problemRepository.saveAll(problemRequests.stream().map(
                problemRequest -> {
                    return Problem.builder()
                            .uuid(problemRequest.uuid())
                            .PType(problemRequest.PType())
                            .EType(problemRequest.EType())
                            .problem(problemRequest.problem())
                            .example(problemRequest.example())
                            .build();
                }
        ).toList());

        problems.forEach(problem -> {
            int index = problems.indexOf(problem);
            questionService.createQuestion(problemRequests.get(index).questions(), problem);
        });

        return problems;
    }

    @Override
    @Transactional
    public ProblemResponse getProblemById(String problemId) {
        Problem problem = problemRepository.findById(problemId);
        return ProblemResponse.builder()
                .problemId(problem.getId())
                .PType(problem.getPType().name())
                .example(problem.getExample())
                .problem(problem.getProblem())
                .questions(QuestionResponse.of(questionService.getQuestionsByProblem(problem)))
                .build();
    }

    @Override
    @Transactional
    public ProblemResponse getProblemByPTypeRandom(RandomProblemRequest request) {
        ProblemType problemType = ProblemType.valueOf(request.problemType().toUpperCase());
        List<Problem> problems = problemRepository.findByPType(problemType).get();
        Random random = new Random();
        Problem selectedProblem = problems.get(random.nextInt(problems.size()));
        return ProblemResponse.builder()
                .problemId(selectedProblem.getId())
                .PType(selectedProblem.getPType().name())
                .example(selectedProblem.getExample())
                .problem(selectedProblem.getProblem())
                .questions(QuestionResponse.of(questionService.getQuestionsByProblem(selectedProblem)))
                .build();
    }

    @Override
    @Transactional
    public void updateProblems(List<ProblemUpdateRequest> requests) {
        requests.forEach(request -> {
            questionService.updateQuestions(request.questions());
        });

        List<String> updateProblemIds = requests.stream()
                .map(ProblemUpdateRequest::problemId)
                .toList();

        List<Problem> problems = problemRepository.findAllByIds(updateProblemIds).get();

        Map<String, ProblemUpdateRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(ProblemUpdateRequest::problemId, request -> request));

        problems.forEach(problem -> {
            ProblemUpdateRequest request = requestMap.get(problem.getId());
            if (request.example() != null) {
                problem.updateProblem(request.example());
            }
        });

        problemRepository.saveAll(problems);
    }

    @Override
    @Transactional
    public List<Question> getQuestions(Problem problem) {
        return questionService.getQuestionsByProblem(problem);
    }

    @Override
    @Transactional
    public void deleteProblem(String problemId) {
        problemRepository.deleteById(problemId);
    }
}
