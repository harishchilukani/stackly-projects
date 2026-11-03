package com.student.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class LoggingAspect {

    @Around("execution(* com.student.service.*.*(..))")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint)
            throws Throwable {

        String methodName = joinPoint.getSignature().getName();

        System.out.println("Started: " + methodName);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();

            long executionTime =
                    System.currentTimeMillis() - startTime;

            System.out.println("Completed: " + methodName);
            System.out.println("Execution time: "
                    + executionTime + " ms");

            return result;

        } catch (Throwable ex) {

            System.out.println("Exception in: " + methodName);
            System.out.println("Error: " + ex.getMessage());

            throw ex;
        }
    }
}