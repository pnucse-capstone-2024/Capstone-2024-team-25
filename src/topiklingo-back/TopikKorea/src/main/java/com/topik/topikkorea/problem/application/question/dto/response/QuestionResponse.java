package com.topik.topikkorea.problem.application.question.dto.response;

import com.topik.topikkorea.problem.application.answer.dto.response.AnswerResponse;
import com.topik.topikkorea.problem.domain.question.Question;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
public class QuestionResponse {
    private final String questionId;
    private final String question;
    private final Integer score;
    @Setter
    private Integer questionNumber;
    private final String example;
    private final List<AnswerResponse> answers;

    public QuestionResponse(String questionId, String question, Integer score, String example, List<AnswerResponse> answers) {
        this.questionId = questionId;
        this.question = question;
        this.score = score;
        this.example = example;
        this.answers = answers; // 불변 리스트로 만듦
    }

    public static List<QuestionResponse> of(final List<Question> questions) {
        return questions.stream()
                .map(question -> new QuestionResponse(
                        question.getId(),
                        question.getQuestionProblem(),
                        question.getScore(),
                        question.getExample(),
                        AnswerResponse.of(question.getAnswers())
                ))
                .collect(Collectors.toList());
    }
}
