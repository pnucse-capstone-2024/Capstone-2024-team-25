package com.topik.topikkorea.problem.api;

import com.topik.topikkorea.problem.application.answer.dto.request.RandomProblemRequest;
import com.topik.topikkorea.problem.application.problem.ProblemService;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.response.ProblemResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @PostMapping("/problem")
    public ResponseEntity<Void> createProblem(@Valid @RequestBody ProblemRequest problemRequest) {
        problemService.createProblem(problemRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/problem/random")
    public ResponseEntity<ProblemResponse> getRandomProblemByPType(@RequestBody RandomProblemRequest request) {
        ProblemResponse problem = problemService.getProblemByPTypeRandom(request);
        return ResponseEntity.ok(problem);
    }

    @GetMapping("/problem/{problemId}")
    public ResponseEntity<ProblemResponse> getProblemById(@PathVariable String problemId) {
        ProblemResponse problem = problemService.getProblemById(problemId);
        return ResponseEntity.ok(problem);
    }
}
