package com.chinaunicom.hsf.annotation;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2019/10/17 12:36
 * @description:
 */

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ProxyConsumer {
    String beanId();

    String version();

    long clientTimeout() default 0L;

    int connectionNum() default 1;

    String group() default "";

    String target() default "";
}
