package com.topik.topikkorea.write.application.question;

import com.topik.topikkorea.write.application.question.dto.request.CreateWriteQuestionRequest;
import com.topik.topikkorea.write.application.question.dto.response.WriteQuestionResponse;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import com.topik.topikkorea.write.domain.question.WriteQuestion;
import java.util.List;

public interface WriteQuestionService {
    List<WriteQuestion> createWriteQuestion(List<CreateWriteQuestionRequest> writeQuestion,
                                            WriteProblem writeProblem);

    WriteQuestion getWriteQuestion(Long id);

    List<WriteQuestionResponse> getWriteQuestions(WriteProblem problem);
}
