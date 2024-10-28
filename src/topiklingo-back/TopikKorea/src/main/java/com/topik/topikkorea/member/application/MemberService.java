package com.topik.topikkorea.member.application;

import com.topik.topikkorea.member.application.dto.request.GoogleLoginRequest;
import com.topik.topikkorea.member.application.dto.request.MemberDetailRequest;
import com.topik.topikkorea.member.application.dto.response.LoginResponse;
import com.topik.topikkorea.member.application.dto.response.MemberInfoResponse;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;

public interface MemberService {
    LoginResponse loginByGoogle(final GoogleLoginRequest request);

    void insertMemberDetail(final Long memberId, final MemberDetailRequest request);

    void insertMemberCenter(Long centerOfferId, Member member);

    void insertMemberGather(Long groupOfferId, Member member);

    List<Member> getAllStudents();

    List<Member> getGatherMembers(Long groupId);

    Member getMember(final Long memberId);

    void updateMemberAuth(String email, String authType);

    MemberInfoResponse getMemberInfo(Long memberId, Member member);
}
