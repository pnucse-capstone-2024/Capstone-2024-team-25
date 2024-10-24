package com.topik.topikkorea.member.annotation;


import com.topik.topikkorea.member.domain.AuthType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthCheck {
    AuthType[] value();
}
