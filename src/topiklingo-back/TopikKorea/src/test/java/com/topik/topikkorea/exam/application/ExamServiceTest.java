package com.topik.topikkorea.exam.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.analyze.application.MemberAnalyzeService;
import com.topik.topikkorea.center.application.CenterService;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.exam.application.dto.request.ExamRequest;
import com.topik.topikkorea.exam.application.dto.request.MemberAnswerRequest;
import com.topik.topikkorea.exam.application.dto.request.PersonalExamRequest;
import com.topik.topikkorea.exam.application.dto.response.ExamIdResponse;
import com.topik.topikkorea.exam.domain.Exam;
import com.topik.topikkorea.exam.domain.ExamProblem;
import com.topik.topikkorea.exam.domain.ExamType;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.exam.domain.MemberExam;
import com.topik.topikkorea.exam.domain.repository.ExamProblemRepository;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.exam.domain.repository.GatherExamRepository;
import com.topik.topikkorea.exam.domain.repository.MemberExamRepository;
import com.topik.topikkorea.exam.exception.ExamException;
import com.topik.topikkorea.exam.exception.ExamExceptionType;
import com.topik.topikkorea.gather.application.GatherService;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.helper.center.CenterFixture;
import com.topik.topikkorea.helper.exam.ExamFixture;
import com.topik.topikkorea.helper.exam.GatherExamFixture;
import com.topik.topikkorea.helper.exam.MemberExamFixture;
import com.topik.topikkorea.helper.exam.PersonalExamRequestFixture;
import com.topik.topikkorea.helper.gather.GatherFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemFixture;
import com.topik.topikkorea.helper.problem.problem.ProblemRequestFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.application.problem.ProblemService;
import com.topik.topikkorea.problem.application.problem.dto.request.PersonalProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.request.ProblemRequest;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ExamServiceTest {
    @Mock
    ExamRepository examRepository;

    @Mock
    ExamProblemRepository examProblemRepository;

    @Mock
    ProblemRepository problemRepository;

    @Mock
    MemberExamRepository memberExamRepository;

    @Mock
    GatherExamRepository gatherExamRepository;

    @Mock
    ProblemService problemService;

    @Mock
    MemberAnalyzeService memberAnalyzeService;

    @Mock
    GatherService gatherService;

    @Mock
    CenterService centerService;

    @InjectMocks
    ExamServiceImpl examService;

    private Exam exam;

    @BeforeEach
    public void init() {
        exam = ExamFixture.testIdExam();
    }

    @Test
    @DisplayName("[success] Exam 생성 성공")
    public void createExam_Exam_생성_성공() {
        // given
        Problem problem = ProblemFixture.testIdProblem();
        Problem problem2 = ProblemFixture.testIdProblem();

        ProblemRequest problemRequest =
                ProblemRequestFixture.testProblemRequest(problem);

        ProblemRequest problemRequest2 =
                ProblemRequestFixture.testProblemRequest(problem2);

        ExamProblem examProblem = ExamProblem.builder()
                .exam(exam)
                .problem(problem)
                .build();

        ExamProblem examProblem2 = ExamProblem.builder()
                .exam(exam)
                .problem(problem2)
                .build();

        ExamRequest examRequest = ExamRequest.builder()
                .uuid(exam.getId())
                .title(exam.getTitle())
                .type(exam.getType().name())
                .year(exam.getYear())
                .tags(List.of())
                .problems(List.of(problemRequest, problemRequest2))
                .build();

        // mocking
        when(problemService.createManyProblems(List.of(problemRequest, problemRequest2)))
                .thenReturn(List.of(problem, problem2));
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examProblemRepository.saveAll(any(List.class))).thenReturn(List.of(examProblem, examProblem2));

        // when
        examService.createExam(examRequest);

        // then
        verify(problemService).createManyProblems(any(List.class));
        verify(examRepository).save(any(Exam.class));
        verify(examProblemRepository).saveAll(any(List.class));
    }

    @Test
    @DisplayName("[success] Personal Exam 생성 성공")
    public void createPersonalExam_PersonalExam_생성_성공() {
        // given
        PersonalExamRequest personalExamRequest = PersonalExamRequestFixture.testPersonalExamRequest();
        List<ExamProblem> examProblems = new java.util.ArrayList<>();
        List<Problem> problems = personalExamRequest.problems().stream()
                .flatMap(req -> {
                    ProblemType problemType = ProblemType.valueOf(req.problemType());
                    return List.of(ProblemFixture.testIdPTypeProblem(problemType)).stream();
                })
                .collect(Collectors.toList());

        problems.forEach(problem -> {
            examProblems.add(ExamProblem.builder()
                    .exam(exam)
                    .problem(problem)
                    .build());
        });

        // mocking
        for (PersonalProblemRequest req : personalExamRequest.problems()) {
            when(problemRepository.findByPType(ProblemType.valueOf(req.problemType())))
                    .thenReturn(Optional.of(problems));
        }
        when(examRepository.save(any(Exam.class))).thenReturn(exam);
        when(examProblemRepository.saveAll(any(List.class))).thenReturn(examProblems);

        // when
        ExamIdResponse examIdResponse = examService.createPersonalExam(personalExamRequest);

        // then
        verify(problemRepository, times(personalExamRequest.problems().size()))
                .findByPType(any(ProblemType.class));
        verify(examRepository).save(any(Exam.class));
        verify(examProblemRepository).saveAll(any(List.class));
        assertThat(examIdResponse).isEqualTo(new ExamIdResponse(exam.getId()));
    }

    @Test
    @DisplayName("[success] Gather 없는 Member Exam 저장 성공")
    public void saveMemberExam_Gather_없는_MemberExam_저장_성공() {
        // given
        Member member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.FEMALE,
                null, null);
        MemberExam memberExam = MemberExamFixture.testIdMemberExam(member, exam);
        MemberAnswerRequest memberAnswerRequest = MemberAnswerRequest.builder()
                .memberAnswers(memberExam.getMemberAnswers())
                .realAnswers(memberExam.getRealAnswers())
                .score(memberExam.getScore())
                .build();

        // mocking
        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(memberExamRepository.save(any(MemberExam.class))).thenReturn(memberExam);

        // when
        examService.saveMemberExamResult(member, exam.getId(), memberAnswerRequest);

        // then
        verify(examRepository).findById(exam.getId());
        verify(memberExamRepository).save(any(MemberExam.class));
    }

    @Test
    @DisplayName("[success] Gather 있는 Member Exam 저장 성공")
    public void saveMemberExam_Gather_있는_MemberExam_저장_성공() {
        // given
        Center center = CenterFixture.testIdCenter();
        Member teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.MALE,
                center, null);
        Gather gather = GatherFixture.testIdGather(center, teacher);
        Member member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE,
                center, gather);
        GatherExam gatherExam = GatherExamFixture.testIdGatherExam(gather, exam);
        MemberExam memberExam = MemberExamFixture.testIdMemberExam(member, exam);
        long gatherExamSum = gatherExam.getSum();
        int gatherExamMemberCount = gatherExam.getMemberCount();
        MemberAnswerRequest memberAnswerRequest = MemberAnswerRequest.builder()
                .memberAnswers(memberExam.getMemberAnswers())
                .realAnswers(memberExam.getRealAnswers())
                .score(memberExam.getScore())
                .build();

        // mocking
        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(memberExamRepository.save(any(MemberExam.class))).thenReturn(memberExam);
        when(gatherExamRepository.findByExamAndGather(exam, gather)).thenReturn(Optional.of(gatherExam));

        // when
        examService.saveMemberExamResult(member, exam.getId(), memberAnswerRequest);

        // then
        verify(examRepository).findById(exam.getId());
        verify(memberExamRepository).save(any(MemberExam.class));
        verify(gatherExamRepository).findByExamAndGather(exam, gather);
        assertThat(gatherExam.getMemberCount()).isEqualTo(gatherExamMemberCount + 1);
        assertThat(gatherExam.getSum()).isEqualTo(gatherExamSum + memberExam.getScore());
    }

    @Test
    @DisplayName("[fail] id로 Exam 조회 실패")
    public void getExamById_id로_Exam_조회_실패() {
        // mocking
        doThrow(new ExamException(ExamExceptionType.EXAM_NOT_FOUND))
                .when(examRepository).findById(exam.getId());

        // when & then
        Assertions.assertThatThrownBy(() -> examService.getExamById(exam.getId()))
                .isInstanceOf(ExamException.class)
                .hasMessage(ExamExceptionType.EXAM_NOT_FOUND.errorMessage());
    }

    @Test
    @DisplayName("[success] Gather Exam 조회 성공")
    public void getGatherExamByGatherAndExam_GatherExam_조회_성공() {
        // given
        Center center = CenterFixture.testIdCenter();
        Member teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.MALE,
                center, null);
        Gather gather = GatherFixture.testIdGather(center, teacher);
        GatherExam gatherExam = GatherExamFixture.testIdGatherExam(gather, exam);

        // mocking
        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(gatherService.getGather(gather.getId())).thenReturn(gather);
        when(gatherExamRepository.findByExamAndGather(exam, gather)).thenReturn(Optional.of(gatherExam));

        // when
        GatherExam result = examService.getGatherExam(exam.getId(), gather.getId());

        // then
        verify(examRepository).findById(exam.getId());
        verify(gatherService).getGather(gather.getId());
        verify(gatherExamRepository).findByExamAndGather(exam, gather);
        assertThat(result).isEqualTo(gatherExam);
    }

    @Test
    @DisplayName("[success] Center Exam 조회 성공")
    public void getCenterExamByCenterAndExam_CenterExam_조회_성공() {
        // given
        Center center = CenterFixture.testIdCenter();
        Member teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.FEMALE,
                center, null);
        Gather gather = GatherFixture.testIdGather(center, teacher);
        Gather gather2 = GatherFixture.testIdGather(center, teacher);
        GatherExam gatherExam = GatherExamFixture.testIdGatherExam(gather, exam);
        GatherExam gatherExam2 = GatherExamFixture.testIdGatherExam(gather2, exam);

        // mocking
        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(centerService.getCenter(center.getId())).thenReturn(center);
        when(gatherExamRepository.findByExamAndCenter(exam, center)).thenReturn(
                Optional.of(List.of(gatherExam, gatherExam2)));

        // when
        List<GatherExam> result = examService.getCenterExam(exam.getId(), center.getId());

        // then
        verify(examRepository).findById(exam.getId());
        verify(centerService).getCenter(center.getId());
        verify(gatherExamRepository).findByExamAndCenter(exam, center);
        assertThat(result).isEqualTo(List.of(gatherExam, gatherExam2));
    }

    @Test
    @DisplayName("[fail] Gather Exam 조회 실패")
    public void getGatherExamByGatherAndExam_GatherExam_조회_실패() {
        // given
        Center center = CenterFixture.testIdCenter();
        Member teacher = MemberFixture.testIdMember(null, AuthType.TEACHER, LoginProvider.GOOGLE.name(), Gender.FEMALE,
                center, null);
        Gather gather = GatherFixture.testIdGather(center, teacher);

        // mocking
        when(examRepository.findById(exam.getId())).thenReturn(Optional.of(exam));
        when(gatherService.getGather(gather.getId())).thenReturn(gather);
        doThrow(new ExamException(ExamExceptionType.GROUP_EXAM_NOT_FOUND))
                .when(gatherExamRepository).findByExamAndGather(exam, gather);

        // when & then
        Assertions.assertThatThrownBy(() -> examService.getGatherExam(exam.getId(), gather.getId()))
                .isInstanceOf(ExamException.class)
                .hasMessage(ExamExceptionType.GROUP_EXAM_NOT_FOUND.errorMessage());
    }

    @Test
    @DisplayName("[fail] type으로 Exam 조회 실패")
    public void getExamByType_type으로_Exam_조회_실패() {
        // mocking
        doThrow(new ExamException(ExamExceptionType.EXAM_NOT_FOUND))
                .when(examRepository).findByType(ExamType.GENERATED);

        // when & then
        Assertions.assertThatThrownBy(() -> examService.getExamsGenerated())
                .isInstanceOf(ExamException.class)
                .hasMessage(ExamExceptionType.EXAM_NOT_FOUND.errorMessage());
    }
}
