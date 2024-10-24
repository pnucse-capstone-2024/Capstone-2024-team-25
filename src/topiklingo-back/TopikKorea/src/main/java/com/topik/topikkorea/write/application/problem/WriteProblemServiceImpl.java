package com.topik.topikkorea.write.application.problem;

import com.topik.topikkorea.write.application.problem.dto.request.CreateWriteProblemRequest;
import com.topik.topikkorea.write.application.problem.dto.response.WriteProblemResponse;
import com.topik.topikkorea.write.application.question.WriteQuestionService;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.problem.repository.WriteProblemRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteProblemServiceImpl implements WriteProblemService {
    private final WriteQuestionService writeQuestionService;
    private final WriteProblemRepository writeProblemRepository;

    @Transactional
    @Override
    public WriteProblem createWriteProblem(CreateWriteProblemRequest request) {
        WriteProblem writeProblem = writeProblemRepository.save(
                WriteProblem.builder()
                        .problem(request.problem())
                        .WType(WriteProblemType.valueOf(request.WType().toUpperCase()))
                        .build()
        );

        writeQuestionService.createWriteQuestion(request.questions(), writeProblem);

        return writeProblem;
    }

    @Override
    public WriteProblemResponse getWriteProblem(Long id) {
        WriteProblem writeProblem = writeProblemRepository.findById(id).orElseThrow();

        return WriteProblemResponse.builder()
                .problem(writeProblem.getProblem())
                .WType(writeProblem.getWType().name())
                .questions(writeQuestionService.getWriteQuestions(writeProblem))
                .build();
    }
}
