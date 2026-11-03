package com.library.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    private static final Logger logger =
            LoggerFactory.getLogger(LoggingAspect.class);

    @Around("execution(* com.library.service.*.*(..))")
    public Object logExecutionTime(
            ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.currentTimeMillis();

        String methodName = joinPoint.getSignature().getName();

        try {

            logger.info("Method execution started: {}", methodName);

            Object result = joinPoint.proceed();

            logger.info("Method execution completed successfully: {}",
                    methodName);

            return result;

        } catch (Throwable exception) {

            logger.error("Exception occurred in method: {}",
                    methodName, exception);

            throw exception;

        } finally {

            long endTime = System.currentTimeMillis();

            long executionTime = endTime - startTime;

            logger.info("Method: {} | Execution Time: {} ms",
                    methodName, executionTime);
        }
    }
}
