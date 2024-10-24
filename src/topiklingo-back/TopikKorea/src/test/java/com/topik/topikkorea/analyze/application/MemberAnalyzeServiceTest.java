package com.topik.topikkorea.analyze.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.analyze.application.dto.request.MemberAnalyzeRequest;
import com.topik.topikkorea.analyze.domain.MemberAnalyze;
import com.topik.topikkorea.analyze.domain.repository.MemberAnalyzeRepository;
import com.topik.topikkorea.helper.analyze.MemberAnalyzeFixture;
import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.problem.domain.problem.ProblemType;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MemberAnalyzeServiceTest {
    @Mock
    private MemberAnalyzeRepository memberAnalyzeRepository;

    @InjectMocks
    private MemberAnalyzeServiceImpl memberAnalyzeService;

    private MemberAnalyze memberAnalyze;

    private MemberAnalyze memberAnalyze2;

    private Member member;

    @BeforeEach
    public void init() {
        member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.FEMALE, null,
                null);
        memberAnalyze = MemberAnalyzeFixture.testIdMemberAnalyze(member);
        memberAnalyze2 = MemberAnalyzeFixture.testIdMemberAnalyze(member);
    }

    @Test
    @DisplayName("[success] 존재하는 유형 업데이트")
    public void insertMemberAnalyze_존재하는_유형_업데이트() {
        // given
        String composition = memberAnalyze.getProblemType().name() + "/" + memberAnalyze.getTotalCount() + "/"
                + memberAnalyze.getCorrectCount();
        String composition2 = memberAnalyze2.getProblemType().name() + "/" + memberAnalyze2.getTotalCount() + "/"
                + memberAnalyze2.getCorrectCount();
        MemberAnalyzeRequest request = new MemberAnalyzeRequest(new String[]{composition, composition2});

        // mocking
        when(memberAnalyzeRepository.findMemberAnalyzesByMemberId(member.getId())).thenReturn(
                Optional.of(List.of(memberAnalyze, memberAnalyze2)));
        doNothing().when(memberAnalyzeRepository)
                .updateByMemberIdAndProblemType(eq(member.getId()), any(ProblemType.class),
                        any(Integer.class), any(Integer.class));

        // when
        memberAnalyzeService.insertMemberAnalyze(request, member);

        // then
        verify(memberAnalyzeRepository, times(2)).updateByMemberIdAndProblemType(eq(member.getId()),
                any(ProblemType.class),
                any(Integer.class), any(Integer.class));
    }

    @Test
    @DisplayName("[success] 존재하지 않는 유형 추가")
    public void insertMemberAnalyze2_존재하지_않는_유형_추가() {
        // given
        String composition = memberAnalyze.getProblemType().name() + "/" + memberAnalyze.getTotalCount() + "/"
                + memberAnalyze.getCorrectCount();
        String composition2 = memberAnalyze2.getProblemType().name() + "/" + memberAnalyze2.getTotalCount() + "/"
                + memberAnalyze2.getCorrectCount();
        MemberAnalyzeRequest request = new MemberAnalyzeRequest(new String[]{composition, composition2});

        // mocking
        when(memberAnalyzeRepository.findMemberAnalyzesByMemberId(member.getId())).thenReturn(
                Optional.of(List.of()));
        when(memberAnalyzeRepository.save(any(MemberAnalyze.class))).thenReturn(any(MemberAnalyze.class));

        // when
        memberAnalyzeService.insertMemberAnalyze(request, member);

        // then
        verify(memberAnalyzeRepository, times(2)).save(any(MemberAnalyze.class));
    }

    @Test
    @DisplayName("[success] 존재 유형 업데이트 및 존재하지 않는 유형 추가")
    public void insertMemberAnalyze3_존재_유형_업데이트_및_존재하지_않는_유형_추가() {
        // given
        String composition = memberAnalyze.getProblemType().name() + "/" + memberAnalyze.getTotalCount() + "/"
                + memberAnalyze.getCorrectCount();
        String composition2 = memberAnalyze2.getProblemType().name() + "/" + memberAnalyze2.getTotalCount() + "/"
                + memberAnalyze2.getCorrectCount();
        MemberAnalyzeRequest request = new MemberAnalyzeRequest(new String[]{composition, composition2});

        // mocking
        when(memberAnalyzeRepository.findMemberAnalyzesByMemberId(member.getId())).thenReturn(
                Optional.of(List.of(memberAnalyze)));
        doNothing().when(memberAnalyzeRepository)
                .updateByMemberIdAndProblemType(member.getId(), memberAnalyze.getProblemType(),
                        memberAnalyze.getTotalCount(), memberAnalyze.getCorrectCount());
        when(memberAnalyzeRepository.save(any(MemberAnalyze.class))).thenReturn(memberAnalyze2);

        // when
        memberAnalyzeService.insertMemberAnalyze(request, member);

        // then
        verify(memberAnalyzeRepository).updateByMemberIdAndProblemType(member.getId(), memberAnalyze.getProblemType(),
                memberAnalyze.getTotalCount(), memberAnalyze.getCorrectCount());
        verify(memberAnalyzeRepository).save(any(MemberAnalyze.class));
    }
}
