package com.topik.topikkorea.analyze.application;

import com.topik.topikkorea.analyze.application.dto.request.MemberAnalyzeRequest;
import com.topik.topikkorea.exam.application.dto.response.MemberAnalyzeResponse;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;

public interface MemberAnalyzeService {
    void insertMemberAnalyze(MemberAnalyzeRequest request, Member member);

    List<MemberAnalyzeResponse> getMemberAnalyze(long member);
}
