package com.topik.topikkorea.exam.application;


import com.topik.topikkorea.analyze.application.MemberAnalyzeService;
import com.topik.topikkorea.center.application.CenterService;
import com.topik.topikkorea.center.domain.Center;
import com.topik.topikkorea.credit.application.CreditService;
import com.topik.topikkorea.credit.application.dto.request.UseCreditRequest;
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
import com.topik.topikkorea.exam.domain.ExamProblem;
import com.topik.topikkorea.exam.domain.ExamType;
import com.topik.topikkorea.exam.domain.ExamWriteProblem;
import com.topik.topikkorea.exam.domain.GatherExam;
import com.topik.topikkorea.exam.domain.MemberExam;
import com.topik.topikkorea.exam.domain.repository.ExamProblemRepository;
import com.topik.topikkorea.exam.domain.repository.ExamRepository;
import com.topik.topikkorea.exam.domain.repository.ExamWriteProblemRepository;
import com.topik.topikkorea.exam.domain.repository.GatherExamRepository;
import com.topik.topikkorea.exam.domain.repository.MemberExamRepository;
import com.topik.topikkorea.exam.exception.ExamException;
import com.topik.topikkorea.exam.exception.ExamExceptionType;
import com.topik.topikkorea.gather.application.GatherService;
import com.topik.topikkorea.gather.domain.Gather;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import com.topik.topikkorea.problem.application.problem.ProblemService;
import com.topik.topikkorea.problem.application.problem.dto.request.PersonalProblemRequest;
import com.topik.topikkorea.problem.application.problem.dto.response.ProblemDetailsResponse;
import com.topik.topikkorea.problem.application.problem.dto.response.ProblemResponse;
import com.topik.topikkorea.problem.application.question.dto.response.QuestionResponse;
import com.topik.topikkorea.problem.domain.problem.Problem;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import com.topik.topikkorea.problem.domain.problem.repository.ProblemRepository;
import com.topik.topikkorea.write.application.problem.WriteProblemService;
import com.topik.topikkorea.write.application.problem.dto.response.WriteProblemResponse;
import com.topik.topikkorea.write.application.question.dto.response.WriteQuestionResponse;
import com.topik.topikkorea.write.domain.WriteProblemType;
import com.topik.topikkorea.write.domain.problem.WriteProblem;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ExamServiceImpl implements ExamService {

    private final ExamRepository examRepository;
    private final ExamProblemRepository examProblemRepository;
    private final ProblemService problemService;
    private final ProblemRepository problemRepository;
    private final MemberExamRepository memberExamRepository;
    private final GatherExamRepository gatherExamRepository;
    private final ExamWriteProblemRepository examWriteProblemRepository;
    private final GatherService gatherService;
    private final CenterService centerService;
    private final MemberAnalyzeService memberAnalyzeService;
    private final CreditService creditService;
    private final WriteProblemService writeProblemService;
    private final HttpRequest httpRequest;

    //createExam method
    @Override
    @Transactional
    public void createExam(ExamRequest examRequest) {
        List<Problem> problems = problemService.createManyProblems(examRequest.problems());

        Exam exam = examRepository.save(
                Exam.builder()
                        .uuid(examRequest.uuid())
                        .title(examRequest.title())
                        .type(examRequest.type())
                        .year(examRequest.year())
                        .build()
        );

        saveExamProblem(exam, problems);
    }

    @Override
    @Transactional
    public ExamIdResponse createPersonalExam(PersonalExamRequest personalExamRequest) {
        List<Problem> selectedProblems = new ArrayList<>();

        for (PersonalProblemRequest request : personalExamRequest.problems()) {
            ProblemType problemType = ProblemType.valueOf(request.problemType().toUpperCase());
            int problemCount = Integer.parseInt(request.problemCount());
            List<Problem> problems = problemRepository.findShuffledPType(problemType);

            List<Problem> availableProblems = new ArrayList<>(problems);
            for (int i = 0; i < problemCount && !availableProblems.isEmpty(); i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(availableProblems.size());
                selectedProblems.add(availableProblems.get(randomIndex));
                availableProblems.remove(randomIndex);
            }
        }

        Exam exam = examRepository.save(
                Exam.builder()
                        .uuid(UUID.randomUUID().toString())
                        .title(personalExamRequest.title())
                        .type(ExamType.GENERATED.toString())
                        .year(LocalDateTime.now().getYear())
                        .build()
        );

        saveExamProblem(exam, selectedProblems);
        return ExamIdResponse.builder()
                .examId(exam.getId())
                .build();
    }

    @Override
    @Transactional
    public void saveExamProblem(Exam exam, List<Problem> problems) {
        examProblemRepository.saveAll(problems.stream()
                .map(problem -> ExamProblem.builder()
                        .exam(exam)
                        .problem(problem)
                        .build()
                ).collect(Collectors.toList()));
    }

    //getExam method
    @Override
    @Transactional
    public ExamResponse getExamById(String id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        if (httpRequest.getCurrentMember().getAuthType() != AuthType.ADMIN) {
            creditService.useCredit(
                    UseCreditRequest.builder()
                            .member(httpRequest.getCurrentMember())
                            .exam(exam)
                            .build()
            );
        }
        return convertExamToExamResponse(exam);
    }

    @Override
    public WriteExamResponse getWriteExamById(String id) {
        Exam exam = examRepository.findById(id)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));

        return ConvertExamToWriteExamResponse(exam);
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExams() {
        List<Exam> exams = examRepository.findAll();
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExamsTopik1Listening() {
        List<Exam> exams = examRepository.findByType(ExamType.TOPIK_1_LISTENING)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExamsTopik1Reading() {
        List<Exam> exams = examRepository.findByType(ExamType.TOPIK_1_READING)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExamsTopik2Listening() {
        List<Exam> exams = examRepository.findByType(ExamType.TOPIK_2_LISTENING)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExamsTopik2Reading() {
        List<Exam> exams = examRepository.findByType(ExamType.TOPIK_2_READING)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<AllExamResponse> getExamsGenerated() {
        List<Exam> exams = examRepository.findByType(ExamType.GENERATED)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        return exams.stream().map(
                exam -> AllExamResponse.builder()
                        .id(exam.getId())
                        .title(exam.getTitle())
                        .year(exam.getYear()).build()
        ).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<RightAnswersResponse> getAnswerByExamId(String examId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        List<Problem> problems = exam.getExamProblems().stream()
                .map(ExamProblem::getProblem)
                .toList();
        List<RightAnswersResponse> rightAnswersResponses = new ArrayList<>();

        problems.forEach(problem ->
                problem.getQuestions().forEach(question -> {
                    rightAnswersResponses.add(RightAnswersResponse.builder()
                            .questionId(question.getId())
                            .score(question.getScore())
                            .rightAnswer(question.getRightAnswer())
                            .build()
                    );
                })
        );

        return rightAnswersResponses;
    }

    @Override
    public void saveWriteExam(CreateWriteExamRequest request) {
        List<WriteProblem> writeProblems = request.problems().stream().map(
                writeProblemService::createWriteProblem
        ).toList();
        Exam exam = examRepository.save(
                Exam.builder()
                        .uuid(request.uuid())
                        .title(request.title())
                        .type(request.type())
                        .year(request.year())
                        .build()
        );

        saveExamWriteProblem(exam, writeProblems);
    }

    @Override
    @Transactional
    public void saveExamWriteProblem(Exam exam, List<WriteProblem> writeProblems) {
        examWriteProblemRepository.saveAll(writeProblems.stream()
                .map(writeProblem -> ExamWriteProblem.builder()
                        .exam(exam)
                        .problem(writeProblem)
                        .build()
                ).collect(Collectors.toList()));
    }

    @Override
    @Transactional
    public void saveMemberExamResult(Member member, String examId, MemberAnswerRequest request) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        memberExamRepository.save(MemberExam.builder()
                .member(member)
                .exam(exam)
                .score(request.score())
                .memberAnswers(request.memberAnswers())
                .realAnswers(request.realAnswers())
                .build());

        memberAnalyzeService.insertMemberAnalyze(request.memberAnalyzeRequest(), member);

        if (member.getGather() != null) {
            saveGatherExamResult(member.getGather(), exam, request.score());
        }
    }

    @Synchronized
    private void saveGatherExamResult(Gather gather, Exam exam, int score) {
        Optional<GatherExam> gatherExam = gatherExamRepository.findByExamAndGather(exam, gather);
        if (gatherExam.isEmpty()) {
            gatherExamRepository.save(GatherExam.builder()
                    .gather(gather)
                    .exam(exam)
                    .memberCount(1)
                    .sum((long) score)
                    .build());
            return;
        }

        gatherExam.get().updateGatherExam(score);
        gatherExamRepository.save(gatherExam.get());
    }

    @Override
    @Transactional
    public GatherExam getGatherExam(String examId, Long gatherId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        Gather gather = gatherService.getGather(gatherId);
        return gatherExamRepository.findByExamAndGather(exam, gather)
                .orElseThrow(() -> new ExamException(ExamExceptionType.GROUP_EXAM_NOT_FOUND));
    }

    @Override
    @Transactional
    public List<GatherExam> getCenterExam(String examId, Long centerId) {
        Exam exam = examRepository.findById(examId)
                .orElseThrow(() -> new ExamException(ExamExceptionType.EXAM_NOT_FOUND));
        Center center = centerService.getCenter(centerId);
        return gatherExamRepository.findByExamAndCenter(exam, center)
                .orElseThrow(() -> new ExamException(ExamExceptionType.GROUP_EXAM_NOT_FOUND));
    }


    @Override
    @Transactional
    //deleteExam method
    public void deleteById(String id) {
        examRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateExam(String id, ExamUpdateRequest request) {
        problemService.updateProblems(request.problems());
    }

    @Override
    public List<ExamRecordResponse> getExamRecord(long member) {
        List<MemberExam> memberExams = memberExamRepository.findByMemberId(member)
                .orElseThrow(() -> new ExamException(ExamExceptionType.MEMBER_EXAM_NOT_FOUND));
        return memberExams.stream().map(
                memberExam -> ExamRecordResponse.builder()
                        .id(memberExam.getId())
                        .examId(memberExam.getExam().getId())
                        .examName(memberExam.getExam().getTitle())
                        .examType(memberExam.getExam().getType().name())
                        .score(memberExam.getScore())
                        .memberAnswers(memberExam.getMemberAnswers())
                        .realAnswers(memberExam.getRealAnswers())
                        .createdAt(memberExam.getCreatedAt())
                        .build()
        ).collect(Collectors.toList());
    }

    @Override
    public List<DepartmentMemberRecordResponse> getDepartmentMembersRecords(DepartmentRecordRequest request) {
        if (request.department().equals("all")) {
            return memberExamRepository.findMemberExamRecordsByDateRange(
                    LocalDateTime.parse(request.startDate()),
                    LocalDateTime.parse(request.endDate())
            );
        }

        return memberExamRepository.findMemberExamRecordsByDepartmentAndDateRange(
                request.department(),
                LocalDateTime.parse(request.startDate()),
                LocalDateTime.parse(request.endDate())
        );
    }

    //dataForm convert method
    private ExamResponse convertExamToExamResponse(Exam exam) {
        List<ProblemResponse> problems = exam.getExamProblems().stream()
                .map(examProblem -> new ProblemResponse(
                        examProblem.getProblem().getId(),
                        examProblem.getProblem().getPType().toString(),
                        examProblem.getProblem().getExample(),
                        examProblem.getProblem().getProblem(),
                        QuestionResponse.of(problemService.getQuestions(examProblem.getProblem()))
                )).toList();

        Map<String, List<ProblemDetailsResponse>> groupedByPType = problems.stream()
                .collect(Collectors.groupingBy(
                        ProblemResponse::PType,
                        Collectors.mapping(
                                problem -> new ProblemDetailsResponse(
                                        problem.problemId(),
                                        problem.problem(),
                                        problem.example(),
                                        problem.questions()
                                ),
                                Collectors.toList()
                        )
                ));
        LinkedHashMap<String, List<ProblemDetailsResponse>> sortedMap = groupedByPType.entrySet().stream()
                .sorted(Entry.comparingByKey(Comparator.comparingInt(key -> ProblemType.valueOf(key).ordinal())))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 충돌하는 키의 경우 기존 값을 유지
                        LinkedHashMap::new // 결과를 LinkedHashMap에 수집하여 순서 유지
                ));

        int number = 1;
        int start = 1;
        for (Entry<String, List<ProblemDetailsResponse>> entry : sortedMap.entrySet()) {
            for (ProblemDetailsResponse problem : entry.getValue()) {
                for (QuestionResponse questionResponse : problem.getQuestions()) {
                    questionResponse.setQuestionNumber(number++);
                }
                String header;
                if (start == number - 1) {
                    header = String.format("[%d]", start);
                } else {
                    header = String.format("[%d~%d]", start, number - 1);
                }
                problem.setProblem(header + problem.getProblem());
                start = number;
            }
        }

        return ExamResponse.builder()
                .examId(exam.getId())
                .title(exam.getTitle())
                .type(exam.getType().toString())
                .year(exam.getYear())
                .totalQuestions(number - 1)
                .config(sortedMap)
                .listenUrl(exam.getListenUrl())
                .build();
    }

    private WriteExamResponse ConvertExamToWriteExamResponse(Exam exam) {
        List<WriteProblem> writeProblems = exam.getExamWriteProblems().stream()
                .map(ExamWriteProblem::getProblem)
                .toList();

        log.info("writeProblems: {}", writeProblems);

        Map<String, List<WriteProblemResponse>> groupedByPType = writeProblems.stream()
                .collect(Collectors.groupingBy(
                        writeProblem -> writeProblem.getWType().toString(), // 수정된 부분
                        Collectors.mapping(
                                writeProblem -> WriteProblemResponse.builder()
                                        .problemId(writeProblem.getId())
                                        .problem(writeProblem.getProblem())
                                        .questions(writeProblem.getQuestions().stream().map(
                                                writeQuestion -> WriteQuestionResponse.builder()
                                                        .id(writeQuestion.getId())
                                                        .question(writeQuestion.getQuestion())
                                                        .questionExample(writeQuestion.getExample())
                                                        .score(writeQuestion.getScore())
                                                        .build()
                                        ).toList())
                                        .WType(writeProblem.getWType().toString())
                                        .build(),
                                Collectors.toList()
                        )
                ));

        log.info("groupedByPType: {}", groupedByPType);

        LinkedHashMap<String, List<WriteProblemResponse>> sortedMap = groupedByPType.entrySet().stream()
                .sorted(Entry.comparingByKey(Comparator.comparingInt(key -> WriteProblemType.valueOf(key).ordinal())))
                .collect(Collectors.toMap(
                        entry -> entry.getKey().toLowerCase(),
                        Entry::getValue,
                        (oldValue, newValue) -> oldValue, // 충돌하는 키의 경우 기존 값을 유지
                        LinkedHashMap::new // 결과를 LinkedHashMap에 수집하여 순서 유지
                ));

        log.info("sortedMap: {}", sortedMap);

        int number = 1;
        int start = 1;
        for (Entry<String, List<WriteProblemResponse>> entry : sortedMap.entrySet()) {
            for (com.topik.topikkorea.write.application.problem.dto.response.WriteProblemResponse problem : entry.getValue()) {
                for (WriteQuestionResponse questionResponse : problem.getQuestions()) {
                    questionResponse.setQuestionNumber(number++);
                }
                String header;
                if (start == number - 1) {
                    header = String.format("[%d]", start);
                } else {
                    header = String.format("[%d~%d]", start, number - 1);
                }
                problem.setProblem(header + problem.getProblem());
                start = number;
            }
        }

        log.info("list sortedMap: {}", sortedMap);

        return WriteExamResponse.builder()
                .examId(exam.getId())
                .title(exam.getTitle())
                .type(exam.getType().toString())
                .year(exam.getYear())
                .totalQuestions(number - 1)
                .config(sortedMap)
                .listenUrl(exam.getListenUrl())
                .build();
    }
}
