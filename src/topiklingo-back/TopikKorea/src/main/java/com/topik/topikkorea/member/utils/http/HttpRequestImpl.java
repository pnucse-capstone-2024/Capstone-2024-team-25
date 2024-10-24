package com.topik.topikkorea.member.utils.http;

import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.domain.repository.MemberRepository;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import com.topik.topikkorea.member.utils.jwt.JwtTokenProvider;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HttpRequestImpl implements HttpRequest {
    private static final ThreadLocal<Member> currentUser = new ThreadLocal<>();

    @Value("${spring.security.jwt.master-key}")
    private String masterKey;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public Member getCurrentMember() {
        if (currentUser.get() != null) {
            return currentUser.get();
        }
        String token = validateFromToken();
        if (masterKey.equals(token)) {
            Member member = memberRepository.findById(6L).get();
            currentUser.set(member);
            return currentUser.get();
        }
        Member member = memberRepository.findById(Long.parseLong(jwtTokenProvider.extractMemberId(token))).get();
        currentUser.set(member);
        return currentUser.get();
    }

    public static void clearCurrentUser() {
        currentUser.remove();
    }

    public String validateFromToken() {
        String validToken = getTokenRequestHeader();
        if (validToken.equals(masterKey)) {
            return validToken;
        }
        jwtTokenProvider.validateToken(validToken);
        return validToken;
    }

    private String getTokenRequestHeader() {
        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }

        return token.substring(7);
    }

    @PostConstruct
    private void init() {
        if (masterKey.startsWith("\"") && masterKey.endsWith("\"")) {
            masterKey = masterKey.substring(1, masterKey.length() - 1);
        }
    }
}
