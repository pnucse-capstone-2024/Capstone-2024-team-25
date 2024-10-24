package com.topik.topikkorea.member.utils.jwt;

import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProviderImpl implements JwtTokenProvider {
    @Value("${spring.security.jwt.secret-key}")
    private String secretKey;
    @Value("${spring.security.jwt.expire-length}")
    private int expireTimeMilliSecond;

    @Override
    public String generateToken(final Member member) {
        return generateToken(member.getId(),
                () -> new Date(new Date().getTime() + expireTimeMilliSecond));
    }

    @Override
    public String generateToken(final Long memberId,
                                final ExpireDateSupplier expireDateSupplier) {
        return Jwts.builder()
                .claim("memberId", memberId)
                .expiration(expireDateSupplier.expireDate())
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public void validateToken(final String token) {
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).build().parseClaimsJws(token).getBody();
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException | UnsupportedJwtException |
                 IllegalArgumentException e) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN);
        }
    }

    @Override
    public String extractMemberId(final String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(secretKey).build()
                    .parseClaimsJws(token)
                    .getBody()
                    .get("memberId")
                    .toString();

        } catch (final ExpiredJwtException exception) {
            throw new MemberException(MemberExceptionType.EXPIRED_ACCESS_TOKEN);
        } catch (final Exception exception) {
            throw new MemberException(MemberExceptionType.INVALID_ACCESS_TOKEN);
        }
    }
}
