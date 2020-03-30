package com.chinaunicom.hsf.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ProxyProvider {
    String version();

    long clientTimeout() default 0L;

    String serializeType() default "hessian";

    int corePoolSize() default 0;

    int maxPoolSize() default 0;

    String group() default "";
}
