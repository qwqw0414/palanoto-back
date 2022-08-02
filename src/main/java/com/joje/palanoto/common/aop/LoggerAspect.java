package com.joje.palanoto.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggerAspect {

    private static final int IGNORE_SIZE = "com.joje.palanoto.".length();

	@Pointcut("execution(* com.joje.palanoto..*.*(..))")
//    @Pointcut("execution(* com.joje.palanoto..*.*(..)) && !execution(* com.joje.palanoto.common..*.*(..))")
    public void pointcut() {}

    @Around("pointcut()")
    public Object loggerAdvice(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String type = signature.getDeclaringTypeName();
        String methodName = signature.getName();

//		공통 경로 제외
        type = type.substring(IGNORE_SIZE);

        log.debug(">> {}.{} >>", type, methodName);
        Object obj = joinPoint.proceed();
        log.debug("<< {}.{} <<", type, methodName);

        return obj;
    }
}
