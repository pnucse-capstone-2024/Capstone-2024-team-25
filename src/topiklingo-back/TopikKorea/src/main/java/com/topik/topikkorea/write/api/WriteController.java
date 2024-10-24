package com.topik.topikkorea.write.api;

import com.topik.topikkorea.member.utils.http.HttpRequest;
import com.topik.topikkorea.write.application.answer.WriteAnswerService;
import com.topik.topikkorea.write.application.answer.dto.request.CreateWriteAnswerRequest;
import com.topik.topikkorea.write.application.answer.dto.request.GraduateWriteAnswerRequest;
import com.topik.topikkorea.write.application.problem.WriteProblemService;
import com.topik.topikkorea.write.application.problem.dto.request.CreateWriteProblemRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Write", description = "쓰기 시험 관련 api")
@RestController
@RequestMapping("/write")
@RequiredArgsConstructor
public class WriteController {
    private final HttpRequest httpRequest;
    private final WriteProblemService writeProblemService;
    private final WriteAnswerService writeAnswerService;

    @Operation(summary = "문제 등록")
    @PostMapping("/problem")
    public ResponseEntity<Void> createProblem(@RequestBody CreateWriteProblemRequest createWriteProblemRequest) {
        writeProblemService.createWriteProblem(createWriteProblemRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "학생 답안 등록")
    @PostMapping("/answer")
    public ResponseEntity<Void> createAnswer(@RequestBody List<CreateWriteAnswerRequest> createWriteAnswerRequests) {
        writeAnswerService.createWriteAnswer(createWriteAnswerRequests, httpRequest.getCurrentMember());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "채점")
    @PostMapping("/graduate")
    public ResponseEntity<Void> graduateAnswer(@RequestBody GraduateWriteAnswerRequest graduateWriteAnswerRequest) {
        writeAnswerService.GraduateWriteAnswer(graduateWriteAnswerRequest);
        return ResponseEntity.ok().build();
    }
}
