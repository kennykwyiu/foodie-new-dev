package com.kenny.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
@Aspect
@Component
public class ServiceLogAspect {

    public static final Logger log = LoggerFactory.getLogger(ServiceLogAspect.class);

    /**
     * AOP Advice:
     * 1. Before Advice: Executes before the method invocation.
     * 2. After Advice: Executes after the method invocation completes normally.
     * 3. Around Advice: Can be executed before and after the method invocation separately.
     * 4. After-Throwing Advice: Executes if an exception occurs during the method invocation.
     * 5. After-Finally Advice: Executes after the method invocation regardless of the outcome.
     */

    /**
     * Aspect Expression:
     * execution represents the expression to be executed
     * The first * represents the method return type, * represents all types
     * The second package name represents the package where the AOP-monitored classes are located
     * The third .. represents this package and all its subpackages
     * The fourth * represents the class name, * represents all classes
     * The fifth *(..) * represents the method name in the class, (..) represents any parameters in the method
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */

    @Around("execution(* com.kenny.service.impl..*.*(..)))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {

        log.info("====== Start executing {}.{} ======",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        // Record the start time
        long begin = System.currentTimeMillis();

        // Execute the target service
        Object result = joinPoint.proceed();

        // Record the end time
        long end = System.currentTimeMillis();
        long takeTime = end - begin;

        if (takeTime > 3000) {
            log.error("====== Execution finished, took: {} milliseconds ======", takeTime);
        } else if (takeTime > 2000) {
            log.warn("====== Execution finished, took: {} milliseconds ======", takeTime);
        } else {
            log.info("====== Execution finished, took: {} milliseconds ======", takeTime);
        }

        return result;

    }
}
