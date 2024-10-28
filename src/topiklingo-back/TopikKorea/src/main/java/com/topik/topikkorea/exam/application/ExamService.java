package com.topik.topikkorea.exam.application;

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
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import java.util.List;

public interface ExamService {
    void createExam(ExamRequest examRequest);

    ExamIdResponse createPersonalExam(PersonalExamRequest personalExamRequest);

    void saveExamProblem(Exam exam, List<Problem> problems);

    ExamResponse getExamById(String id);

    WriteExamResponse getWriteExamById(String id);

    List<AllExamResponse> getExams();

    List<AllExamResponse> getExamsTopik1Listening();

    List<AllExamResponse> getExamsTopik1Reading();

    List<AllExamResponse> getExamsTopik2Listening();

    List<AllExamResponse> getExamsTopik2Reading();

    List<AllExamResponse> getExamsGenerated();

    List<RightAnswersResponse> getAnswerByExamId(String examId);

    void saveWriteExam(CreateWriteExamRequest request);

    void saveExamWriteProblem(Exam exam, List<WriteProblem> problems);

    void saveMemberExamResult(Member member, String examId, MemberAnswerRequest request);

    GatherExam getGatherExam(String examId, Long gatherId);

    List<GatherExam> getCenterExam(String examId, Long centerId);

    void deleteById(String id);

    void updateExam(String id, ExamUpdateRequest request);

    List<ExamRecordResponse> getExamRecord(long memberId);

    List<DepartmentMemberRecordResponse> getDepartmentMembersRecords(final DepartmentRecordRequest request);

}
