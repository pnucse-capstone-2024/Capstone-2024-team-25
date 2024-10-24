package com.topik.topikkorea.problem.application.answer;

import com.topik.topikkorea.problem.application.answer.dto.request.AnswerCreateRequest;
import com.topik.topikkorea.problem.application.answer.dto.request.AnswerUpdateRequest;
import com.topik.topikkorea.problem.domain.answer.Answer;
import com.topik.topikkorea.problem.domain.answer.repository.AnswerRepository;
import com.topik.topikkorea.problem.domain.question.Question;
import com.topik.topikkorea.problem.exception.answer.AnswerException;
import com.topik.topikkorea.problem.exception.answer.AnswerExceptionType;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class AnswerServiceImpl implements AnswerService {
    private final AnswerRepository answerRepository;

    @Override
    @Transactional
    public void createAnswer(final List<AnswerCreateRequest> answerCreateRequests, Question question) {
        List<Answer> answers = answerCreateRequests.stream()
                .map(answerCreateRequest -> Answer.builder()
                        .uuid(answerCreateRequest.uuid())
                        .answer(answerCreateRequest.answer())
                        .AType(answerCreateRequest.AType())
                        .question(question)
                        .build())
                .collect(Collectors.toList());

        answerRepository.saveAll(answers);
    }

    @Override
    @Transactional
    public void updateAnswers(final List<AnswerUpdateRequest> requests) {
        List<String> answerIds = requests.stream()
                .map(AnswerUpdateRequest::answerId)
                .toList();

        List<Answer> answers = answerRepository.findAllByIds(answerIds)
                .orElseThrow(() -> new AnswerException(AnswerExceptionType.ANSWER_NOT_FOUND));

        Map<String, AnswerUpdateRequest> requestMap = requests.stream()
                .collect(Collectors.toMap(AnswerUpdateRequest::answerId, request -> request));

        answers.forEach(answer -> {
            AnswerUpdateRequest request = requestMap.get(answer.getId());
            if (request != null) {
                answer.update(request.answer());
            }
        });

        answerRepository.saveAll(answers);
    }
}
