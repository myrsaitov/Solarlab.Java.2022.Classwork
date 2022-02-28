package ru.solarlab.study.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ValidationException;

/**
 *
 */
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class UserCannotDeleteTasksException extends ValidationException {

    /** Сообщение об ошибке по умолчанию */
    private static final String DEFAULT_ERROR_MESSAGE = "Пользователи не могут удалять задачи";

    public UserCannotDeleteTasksException() {
        super(DEFAULT_ERROR_MESSAGE);
    }

}
