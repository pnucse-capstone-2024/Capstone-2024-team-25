package com.topik.topikkorea.write.application.problem;

import com.topik.topikkorea.write.application.problem.dto.request.CreateWriteProblemRequest;
import com.topik.topikkorea.write.application.problem.dto.response.WriteProblemResponse;
import com.topik.topikkorea.write.domain.problem.WriteProblem;

public interface WriteProblemService {
    WriteProblem createWriteProblem(CreateWriteProblemRequest writeProblem);

    WriteProblemResponse getWriteProblem(Long id);
}
