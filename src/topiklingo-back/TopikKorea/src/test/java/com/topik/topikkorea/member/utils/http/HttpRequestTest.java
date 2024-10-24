package com.topik.topikkorea.member.utils.http;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import com.topik.topikkorea.member.utils.jwt.JwtTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
public class HttpRequestTest {
    @Mock
    private MemberRepository memberRepository;

    @Mock
    private HttpServletRequest httpServletRequest;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @InjectMocks
    private HttpRequestImpl httpRequest;

    private Member member;

    private Member master;

    private final String validToken = "valid-token";

    private final String masterKey = "master-key";

    private final String bearer = "Bearer ";

    private final String authorization = "Authorization";

    @BeforeEach
    public void init() {
        HttpRequestImpl.clearCurrentUser();
        member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE, null,
                null);
        master = MemberFixture.testIdMember(6L, AuthType.ADMIN, LoginProvider.GOOGLE.name(), Gender.MALE, null, null);
        ReflectionTestUtils.setField(httpRequest, "masterKey", masterKey);
    }

    @Test
    @Transactional
    @DisplayName("[success] 현재 사용자 조회")
    public void 현재_사용자_조회() {
        // when
        when(httpServletRequest.getHeader(authorization)).thenReturn(bearer + validToken);
        doNothing().when(jwtTokenProvider).validateToken(validToken);
        when(jwtTokenProvider.extractMemberId(validToken)).thenReturn(member.getId().toString());
        when(memberRepository.findById(member.getId())).thenReturn(Optional.ofNullable(member));
        // then
        assertThat(httpRequest.getCurrentMember()).isEqualTo(member);
    }

    @Test
    @DisplayName("[success] 현재 사용자 조회 - 마스터 토큰")
    public void 현재_사용자_조회_마스터_토큰() {
        // when
        when(httpServletRequest.getHeader(authorization)).thenReturn(bearer + masterKey);
        when(memberRepository.findById(6L)).thenReturn(Optional.ofNullable(master));
        // then
        assertThat(httpRequest.getCurrentMember()).isEqualTo(master);
    }

    @Test
    @DisplayName("[success] 토큰 유효 조회")
    public void 토큰_유호_조회() {
        // mocking
        when(httpServletRequest.getHeader(authorization)).thenReturn(bearer + validToken);
        doNothing().when(jwtTokenProvider).validateToken(validToken);

        // when
        httpRequest.validateFromToken();
    }

    @Test
    @DisplayName("[fail] 토큰 유효 조회 - 토큰 유효기간 지남")
    public void 토큰_유호_조회_토큰_유효기간_지남() {
        // given
        String expiredToken = "expired-token";

        // mocking
        when(httpServletRequest.getHeader(authorization)).thenReturn(bearer + expiredToken);
        doThrow(new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN))
                .when(jwtTokenProvider).validateToken(expiredToken);

        // when
        assertThatThrownBy(() -> httpRequest.validateFromToken())
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.EXPIRED_ACCESS_TOKEN.errorMessage());
    }
}
