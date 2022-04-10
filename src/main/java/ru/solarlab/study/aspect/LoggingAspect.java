package ru.solarlab.study.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
@ConditionalOnExpression("${aspect.logging.enabled:false}")
public class LoggingAspect {

    private final ObjectMapper objectMapper;

    @Before("execution(* ru.solarlab.study.controller..*(..)))")
    public void controllerMethods(JoinPoint joinPoint) {

        String jsonArgsAsString = "";
        String packageName = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        try {
            jsonArgsAsString = objectMapper.writeValueAsString(joinPoint.getArgs());
        } catch (Throwable ex) {
            log.warn("Ошибка сериализации параметров метода в JSON: {}", ex.getMessage());
            jsonArgsAsString = "[undefined]";
        }
        log.trace("{}.{}[{}]", packageName, methodName, jsonArgsAsString);
    }
}
