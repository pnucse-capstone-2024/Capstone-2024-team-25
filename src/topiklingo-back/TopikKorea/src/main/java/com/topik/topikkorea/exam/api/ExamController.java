package com.topik.topikkorea.exam.api;

import com.topik.topikkorea.exam.application.ExamService;
import com.topik.topikkorea.exam.application.dto.request.CreateWriteExamRequest;
import com.topik.topikkorea.exam.application.dto.request.DepartmentRecordRequest;
import com.topik.topikkorea.exam.application.dto.request.ExamRequest;
import com.topik.topikkorea.exam.application.dto.request.ExamUpdateRequest;
import com.topik.topikkorea.exam.application.dto.request.MemberAnswerRequest;
import com.topik.topikkorea.exam.application.dto.request.PersonalExamRequest;
import com.topik.topikkorea.exam.application.dto.response.AllExamResponse;
import com.topik.topikkorea.exam.application.dto.response.DepartmentMemberRecordResponse;
import com.topik.topikkorea.exam.application.dto.response.ExamIdResponse;
import com.topik.topikkorea.exam.application.dto.response.ExamRecordResponse;
import com.topik.topikkorea.exam.application.dto.response.ExamResponse;
import com.topik.topikkorea.exam.application.dto.response.RightAnswersResponse;
import com.topik.topikkorea.exam.application.dto.response.WriteExamResponse;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Exam", description = "시험 관련 api")
@RestController
@Slf4j
@RequiredArgsConstructor
public class ExamController {
    private final ExamService examService;
    private final HttpRequest httpRequest;

    // 문제 생성 컨트롤러
    @Operation(summary = "시험 등록")
    @AuthCheck({AuthType.ADMIN})
    @PostMapping("/exam")
    public ResponseEntity<Void> createExam(@Valid @RequestBody ExamRequest examRequest) {
        examService.createExam(examRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Personal 시험 생성")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @PostMapping("/exam/generated")
    public ResponseEntity<ExamIdResponse> createPersonalExam(
            @Valid @RequestBody PersonalExamRequest personalExamRequest) {
        ExamIdResponse examId = examService.createPersonalExam(personalExamRequest);
        return ResponseEntity.ok(examId);
    }

    @Operation(summary = "Write 시험 생성")
    @PostMapping("/exam/write")
    public ResponseEntity<Void> createWriteExam(@Valid @RequestBody CreateWriteExamRequest request) {
        examService.saveWriteExam(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "ExamId를 이용한 시험 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/exam/{examId}")
    public ResponseEntity<ExamResponse> getExamById(@PathVariable String examId) {
        final ExamResponse exam = examService.getExamById(examId);
        return ResponseEntity.ok(exam);
    }

    @Operation(summary = "ExamId를 이용한 쓰기 시험 조회")
    @GetMapping("/exam/write/{examId}")
    public ResponseEntity<WriteExamResponse> getWriteExamById(@PathVariable String examId) {
        final WriteExamResponse exam = examService.getWriteExamById(examId);
        log.info("exam: {}", exam);
        return ResponseEntity.ok(exam);
    }

    @Operation(summary = "시험 전체 조회")
    @GetMapping("/exam")
    public ResponseEntity<List<AllExamResponse>> getExams() {
        List<AllExamResponse> exams = examService.getExams();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "topik1-reading 시험 전체 조회")
    @GetMapping("/exam/topik1-reading")
    public ResponseEntity<List<AllExamResponse>> getExamsTopik1Reading() {
        List<AllExamResponse> exams = examService.getExamsTopik1Reading();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "topik1-listening 시험 전체 조회")
    @GetMapping("/exam/topik1-listening")
    public ResponseEntity<List<AllExamResponse>> getExamsTopik1Listening() {
        List<AllExamResponse> exams = examService.getExamsTopik1Listening();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "topik2-reading 시험 전체 조회")
    @GetMapping("/exam/topik2-reading")
    public ResponseEntity<List<AllExamResponse>> getExamsTopik2Reading() {
        List<AllExamResponse> exams = examService.getExamsTopik2Reading();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "topik2-listening 시험 전체 조회")
    @GetMapping("/exam/topik2-listening")
    public ResponseEntity<List<AllExamResponse>> getExamsTopik2Listening() {
        List<AllExamResponse> exams = examService.getExamsTopik2Listening();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "generated 시험 전체 조회")
    @GetMapping("/exam/generated")
    public ResponseEntity<List<AllExamResponse>> getExamsGenerated() {
        List<AllExamResponse> exams = examService.getExamsGenerated();
        return ResponseEntity.ok(exams);
    }

    @Operation(summary = "examId 시험 정답 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/exam-answer/{examId}")
    public ResponseEntity<List<RightAnswersResponse>> getAnswersByExamId(@PathVariable String examId) {
        List<RightAnswersResponse> rightAnswers = examService.getAnswerByExamId(examId);
        return ResponseEntity.ok(rightAnswers);
    }

    @Operation(summary = "examId 시험 정답지 제출 및 결과 저장")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @PostMapping("/exam-answer/{examId}")
    public ResponseEntity<Void> saveMemberExamResult(@PathVariable String examId,
                                                     @RequestBody MemberAnswerRequest request) {
        log.info("1");
        examService.saveMemberExamResult(httpRequest.getCurrentMember(), examId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "memberId 시험 결과 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/exam-record/{memberId}")
    public ResponseEntity<List<ExamRecordResponse>> getExamRecord(@PathVariable long memberId) {
        List<ExamRecordResponse> examRecords = examService.getExamRecord(memberId);
        return ResponseEntity.ok(examRecords);
    }

    @Operation(summary = "examId 시험 그룹별 결과 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/exam/{examId}/group/{groupId}")
    public ResponseEntity<GatherExam> getGroupExam(@PathVariable String examId, @PathVariable Long groupId) {
        GatherExam gatherExam = examService.getGatherExam(examId, groupId);
        return ResponseEntity.ok(gatherExam);
    }

    @Operation(summary = "examId 시험 센터별 결과 조회")
    @AuthCheck({AuthType.ADMIN, AuthType.CENTER, AuthType.TEACHER, AuthType.STUDENT})
    @GetMapping("/exam/{examId}/center/{centerId}")
    public ResponseEntity<List<GatherExam>> getCenterExam(@PathVariable String examId, @PathVariable Long centerId) {
        List<GatherExam> gatherExams = examService.getCenterExam(examId, centerId);
        return ResponseEntity.ok(gatherExams);
    }

    @Operation(summary = "examId 시험 삭제")
    @AuthCheck({AuthType.ADMIN})
    @DeleteMapping("/exam/{examId}")
    public ResponseEntity<Void> deleteExamById(@PathVariable String examId) {
        examService.deleteById(examId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @Operation(summary = "examId 시험 업데이트 - modify")
    @AuthCheck({AuthType.ADMIN})
    @PatchMapping("/exam/{examId}")
    public ResponseEntity<Void> updateExam(@PathVariable String examId, @RequestBody ExamUpdateRequest request) {
        examService.updateExam(examId, request);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "소속별 기간 기록 조회")
    @AuthCheck({AuthType.ADMIN})
    @PostMapping("/exam/department/record")
    public ResponseEntity<List<DepartmentMemberRecordResponse>> getRecord(
            @RequestBody DepartmentRecordRequest request) {
        List<DepartmentMemberRecordResponse> records = examService.getDepartmentMembersRecords(request);
        return ResponseEntity.ok(records);
    }
}
