package com.chinaunicom.hsf.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2019/10/17 12:38
 * @description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ProxyMethod {
}
