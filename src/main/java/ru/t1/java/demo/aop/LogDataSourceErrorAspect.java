package ru.t1.java.demo.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LogDataSourceErrorAspect {

    @Pointcut("within(ru.t1.java.demo.controller.*)")
    public void controllerMethods() {

    }

    @AfterThrowing(pointcut = "@annotation(ru.t1.java.demo.aop.LogDataSourceError)")
    @Order(0)
    public void logDataSourceErrorAnnotation(JoinPoint joinPoint) {
        log.error("ASPECT AFTER THROWING ANNOTATION: Data source exception: {}", joinPoint.getSignature().getName());
    }
}
