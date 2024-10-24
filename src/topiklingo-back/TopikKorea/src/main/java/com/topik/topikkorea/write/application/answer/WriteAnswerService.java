package com.topik.topikkorea.write.application.answer;

import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.write.application.answer.dto.request.CreateWriteAnswerRequest;
import com.topik.topikkorea.write.application.answer.dto.request.GraduateWriteAnswerRequest;
import java.util.List;

public interface WriteAnswerService {
    void createWriteAnswer(List<CreateWriteAnswerRequest> request, Member member);

    void GraduateWriteAnswer(GraduateWriteAnswerRequest request);
}
