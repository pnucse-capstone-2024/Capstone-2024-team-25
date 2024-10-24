package com.topik.topikkorea.member.annotation.aspect;

import com.topik.topikkorea.member.annotation.AuthCheck;
import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Member;
import com.topik.topikkorea.member.exception.MemberException;
import com.topik.topikkorea.member.exception.MemberExceptionType;
import com.topik.topikkorea.member.utils.http.HttpRequest;
import java.lang.reflect.Method;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthCheckAspect {

    @Autowired
    private HttpRequest httpRequest;

    @Before("@annotation(com.topik.topikkorea.member.annotation.AuthCheck)")
    public void checkAuth(JoinPoint joinPoint) {
        Member currentUser = httpRequest.getCurrentMember();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        AuthCheck authCheck = method.getAnnotation(AuthCheck.class);

        AuthType[] requiredAuths = authCheck.value();
        boolean hasRequiredAuth = false;

        for (AuthType requiredAuth : requiredAuths) {
            if (currentUser.getAuthType().compareTo(requiredAuth) >= 0) {
                hasRequiredAuth = true;
                break;
            }
        }

        if (!hasRequiredAuth) {
            throw new MemberException(MemberExceptionType.UNAUTHORIZED_PERMISSION);
        }
    }
}
