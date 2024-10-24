package com.topik.topikkorea.analyze.api;

import com.topik.topikkorea.analyze.application.MemberAnalyzeService;
import com.topik.topikkorea.exam.application.dto.response.MemberAnalyzeResponse;
import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.domain.AuthType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Analyze", description = "분석 관련 api")
@RestController
@RequestMapping("/analyze")
@RequiredArgsConstructor
public class MemberAnalyzeController {
    private final MemberAnalyzeService memberAnalyzeService;

    @Operation(summary = "member 학습 분석 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/{memberId}")
    public ResponseEntity<List<MemberAnalyzeResponse>> getMemberAnalyze(@PathVariable long memberId) {
        List<MemberAnalyzeResponse> memberAnalyzes = memberAnalyzeService.getMemberAnalyze(memberId);
        return ResponseEntity.ok(memberAnalyzes);
    }
}
