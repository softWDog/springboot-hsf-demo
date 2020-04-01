package com.chinaunicom.detail.aspect;

import com.chinaunicom.detail.constant.BaseConstant;
import com.chinaunicom.detail.controller.reqVo.ResultVo;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/1 14:27
 * @description:
 */
@Aspect
@Component
public class LogAspect implements Ordered {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Pointcut("execution(public * com.chinaunicom.detail.controller.*.*(..))")
    public void pointCut(){}

    @Around(value = "pointCut()")
    public Object around(ProceedingJoinPoint pjp){
        ResultVo resultVo;
        try {
            Object[] objects=pjp.getArgs();
            logger.info(pjp.getTarget().getClass().getName()+"执行,入参:");
            resultVo=(ResultVo) pjp.proceed();
            resultVo.setResCode(BaseConstant.SUCCESS_CODE);
            resultVo.setResDesc("请求成功");
        } catch (Throwable e) {
            logger.error("执行异常", e);
            resultVo=new ResultVo();
            resultVo.setResCode(BaseConstant.FAIL_CODE);
            resultVo.setResDesc("请求异常，"+e.getMessage());
        }
        return resultVo;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
