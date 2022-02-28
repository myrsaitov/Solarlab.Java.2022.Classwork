package ru.solarlab.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;
import java.lang.invoke.MethodHandles;

/**
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class CannotEditOtherTasksException extends ValidationException {

    /** Сообщение об ошибке по умолчанию */
    private static final String DEFAULT_ERROR_MESSAGE = "Нельзя редактировать задачи других пользователей";

    public CannotEditOtherTasksException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
