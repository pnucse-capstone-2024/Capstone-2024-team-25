package com.topik.topikkorea.write.application.answer;

import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.write.application.answer.dto.request.CreateWriteAnswerRequest;
import com.topik.topikkorea.write.application.answer.dto.request.GraduateWriteAnswerRequest;
import com.topik.topikkorea.write.application.question.WriteQuestionService;
import com.topik.topikkorea.write.domain.answer.WriteAnswer;
import com.topik.topikkorea.write.domain.answer.WriteAnswerBundle;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerBundleRepository;
import com.topik.topikkorea.write.domain.answer.repository.WriteAnswerRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WriteAnswerServiceImpl implements WriteAnswerService {
    private final WriteAnswerRepository writeAnswerRepository;
    private final WriteAnswerBundleRepository writeAnswerBundleRepository;
    private final WriteQuestionService writeQuestionService;

    @Transactional
    @Override
    public void createWriteAnswer(List<CreateWriteAnswerRequest> requests, Member member) {
        WriteAnswerBundle bundle = writeAnswerBundleRepository.save(
                WriteAnswerBundle.builder()
                        .member(member)
                        .build()
        );

        requests.stream().map(
                request -> WriteAnswer.builder()
                        .answer(request.answer())
                        .question(writeQuestionService.getWriteQuestion((long) request.questionId()))
                        .member(member)
                        .bundle(bundle)
                        .build()
        ).forEach(writeAnswerRepository::save);
    }

    @Transactional
    @Override
    public void GraduateWriteAnswer(GraduateWriteAnswerRequest request) {
        WriteAnswer writeAnswer = writeAnswerRepository.findById((long) request.answerId()).orElseThrow();
        writeAnswer.gradingAnswer(request.score(), request.reason());
        if (!writeAnswer.getBundle().isGraduated()) {
            writeAnswerBundleRepository.updateIsGraduated(writeAnswer.getBundle().getId(), true);
        }

        writeAnswerRepository.save(writeAnswer);
    }


}
