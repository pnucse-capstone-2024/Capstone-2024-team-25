package com.topik.topikkorea.member.utils.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

import com.topik.topikkorea.helper.member.MemberFixture;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Gender;
import com.topik.topikkorea.member.domain.LoginProvider;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class JwtTokenProviderTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private Member member;

    @BeforeEach
    public void init() {
        member = MemberFixture.testIdMember(null, AuthType.STUDENT, LoginProvider.GOOGLE.name(), Gender.MALE, null,
                null);
    }

    @Test
    @DisplayName("[success] 로그인 성공 - 토큰 생성")
    void 로그인_성공_토큰_생성() {
        // mocking
        String testToken = "testToken";
        when(jwtTokenProvider.generateToken(member)).thenReturn(testToken);

        // when
        final String generatedToken = jwtTokenProvider.generateToken(member);

        // then
        assertThat(generatedToken).isEqualTo(testToken);
    }

    @Test
    @DisplayName("[fail] 토큰 만료")
    void token_expired_not_valid() {
        // given
        final String token = "expiredToken";
        final LocalDateTime now = LocalDateTime.now();
        final long expireTime = 100L;

        ExpireDateSupplier expireDateSupplier = () -> Date.from(LocalDateTime.from(now.minusSeconds(expireTime))
                .atZone(ZoneId.systemDefault()).toInstant());

        // mocking
        when(jwtTokenProvider.generateToken(member.getId(), expireDateSupplier)).thenReturn(token);
        doThrow(new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN)).when(jwtTokenProvider)
                .validateToken(token);

        // when
        final String generatedToken = jwtTokenProvider.generateToken(member.getId(), expireDateSupplier);

        // then
        assertThatThrownBy(() -> jwtTokenProvider.validateToken(generatedToken))
                .isInstanceOf(MemberException.class)
                .hasMessage(MemberExceptionType.EXPIRED_ACCESS_TOKEN.errorMessage());
    }
}
