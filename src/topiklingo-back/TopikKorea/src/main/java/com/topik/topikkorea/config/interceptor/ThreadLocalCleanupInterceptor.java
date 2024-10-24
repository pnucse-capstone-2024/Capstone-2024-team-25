package com.topik.topikkorea.config.interceptor;

import com.topik.topikkorea.member.utils.http.HttpRequestImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ThreadLocalCleanupInterceptor implements HandlerInterceptor {
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        HttpRequestImpl.clearCurrentUser();
    }
}