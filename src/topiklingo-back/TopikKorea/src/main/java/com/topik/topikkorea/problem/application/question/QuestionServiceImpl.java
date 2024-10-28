package com.topik.topikkorea.problem.application.question;

import com.topik.topikkorea.problem.application.answer.AnswerService;
import com.topik.topikkorea.problem.application.question.dto.request.QuestionCreateRequest;
import com.topik.topikkorea.problem.application.question.dto.request.QuestionUpdateRequest;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.domain.question.repository.QuestionRepository;
import com.topik.topikkorea.problem.exception.question.QuestionException;
import com.topik.topikkorea.problem.exception.question.QuestionExceptionType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class QuestionServiceImpl implements QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerService answerService;

    @Override
    @Transactional
    public void createQuestion(final List<QuestionCreateRequest> questionCreateRequests, Problem problem) {
        List<Question> questions = questionRepository.saveAll(questionCreateRequests.stream()
                .map(questionCreateRequest -> {
                    return Question.builder()
                            .uuid(questionCreateRequest.uuid())
                            .QEType(questionCreateRequest.QEType())
                            .questionProblem(questionCreateRequest.questionProblem())
                            .score(questionCreateRequest.score())
                            .rightAnswer(questionCreateRequest.rightAnswer())
                            .explain(questionCreateRequest.explain())
                            .example(questionCreateRequest.example())
                            .problem(problem)
                            .build();
                })
                .collect(Collectors.toList()));

        questions.forEach(question -> {
            int index = questions.indexOf(question);
            answerService.createAnswer(questionCreateRequests.get(index).answers(), question);
        });
    }

    @Override
    @Transactional
    public List<Question> getQuestionsByProblem(Problem problem) {
        return questionRepository.getQuestionsByProblem(problem)
                .orElseThrow(() -> new QuestionException(QuestionExceptionType.QUESTION_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updateQuestions(final List<QuestionUpdateRequest> requests) {
        requests.forEach(request -> {
            answerService.updateAnswers(request.answers());
        });

        List<String> questionIds = requests.stream()
                .map(QuestionUpdateRequest::questionId)
                .toList();

        List<Question> questions = questionRepository.findAllByIds(questionIds)
                .orElseThrow(() -> new QuestionException(QuestionExceptionType.QUESTION_NOT_FOUND));

        Map<String, QuestionUpdateRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(QuestionUpdateRequest::questionId, request -> request));

        questions.forEach(question -> {
            QuestionUpdateRequest request = requestMap.get(question.getId());
            if (request != null) {
                question.update(request.question(), request.example());
            }
        });

        questionRepository.saveAll(questions);
    }
}
