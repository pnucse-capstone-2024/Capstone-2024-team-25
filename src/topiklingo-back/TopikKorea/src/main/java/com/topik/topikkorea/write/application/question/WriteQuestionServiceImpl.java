package com.topik.topikkorea.write.application.question;

import com.topik.topikkorea.write.application.question.dto.request.CreateWriteQuestionRequest;
import com.topik.topikkorea.write.application.question.dto.response.WriteQuestionResponse;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import com.topik.topikkorea.write.domain.question.repository.WriteQuestionRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteQuestionServiceImpl implements WriteQuestionService {
    private final WriteQuestionRepository writeQuestionRepository;

    @Transactional
    @Override
    public List<WriteQuestion> createWriteQuestion(List<CreateWriteQuestionRequest> requests,
                                                   WriteProblem writeProblem) {
        List<WriteQuestion> writeQuestions = requests.stream().map(request -> WriteQuestion.builder()
                .question(request.question())
                .example(request.example())
                .score(request.score())
                .problem(writeProblem)
                .build()
        ).collect(Collectors.toList());

        writeQuestionRepository.saveAll(writeQuestions);

        return writeQuestions;
    }

    public WriteQuestion getWriteQuestion(Long id) {
        return writeQuestionRepository.findById(id).orElseThrow();
    }

    @Override
    public List<WriteQuestionResponse> getWriteQuestions(WriteProblem problem) {

        return writeQuestionRepository.findAllByProblem(problem).stream().map(
                writeQuestion -> WriteQuestionResponse.builder()
                        .id(writeQuestion.getId())
                        .question(writeQuestion.getQuestion())
                        .questionExample(writeQuestion.getExample())
                        .score(writeQuestion.getScore())
                        .build()
        ).collect(Collectors.toList());
    }
}
