package com.topik.topikkorea.analyze.application;

import com.topik.topikkorea.analyze.application.dto.request.MemberAnalyzeRequest;
import com.topik.topikkorea.analyze.domain.MemberAnalyze;
import com.topik.topikkorea.analyze.domain.repository.MemberAnalyzeRepository;
import com.topik.topikkorea.analyze.exception.MemberAnalyzeException;
import com.topik.topikkorea.analyze.exception.MemberAnalyzeExceptionType;
import com.topik.topikkorea.exam.application.dto.response.MemberAnalyzeResponse;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberAnalyzeServiceImpl implements MemberAnalyzeService {
    @Autowired
    private MemberAnalyzeRepository memberAnalyzeRepository;

    @Override
    @Transactional
    public void insertMemberAnalyze(MemberAnalyzeRequest request, Member member) {
        String[] compositions = request.MemberAnalyze();
        List<MemberAnalyze> memberAnalyzes = memberAnalyzeRepository.findMemberAnalyzesByMemberId(member.getId())
                .orElseGet(List::of);
        List<ProblemType> problemTypes = memberAnalyzes.stream().map(MemberAnalyze::getProblemType).toList();
        Arrays.stream(compositions).forEach(composition -> {
            String[] elements = composition.split("/");
            if (problemTypes.contains(ProblemType.valueOf(elements[0]))) {
                memberAnalyzeRepository.updateByMemberIdAndProblemType(
                        member.getId(),
                        ProblemType.valueOf(elements[0]),
                        Integer.parseInt(elements[1]),
                        Integer.parseInt(elements[2])
                );
            } else {
                MemberAnalyze memberAnalyze = MemberAnalyze.builder()
                        .member(member)
                        .problemType(ProblemType.valueOf(elements[0]))
                        .totalCount(Integer.parseInt(elements[1]))
                        .correctCount(Integer.parseInt(elements[2]))
                        .build();
                memberAnalyzeRepository.save(memberAnalyze);
            }
        });
    }

    @Override
    public List<MemberAnalyzeResponse> getMemberAnalyze(long memberId) {
        List<MemberAnalyze> memberAnalyzes = memberAnalyzeRepository.findMemberAnalyzesByMemberId(memberId)
                .orElseThrow(() -> new MemberAnalyzeException(MemberAnalyzeExceptionType.NOT_FOUND_MEMBER_ANALYZE));

        return memberAnalyzes.stream().map(
                memberAnalyze -> MemberAnalyzeResponse.builder()
                        .problemType(memberAnalyze.getProblemType().name())
                        .totalCount(memberAnalyze.getTotalCount())
                        .correctCount(memberAnalyze.getCorrectCount())
                        .id(memberAnalyze.getId())
                        .build()
        ).toList(
        );
    }
}
